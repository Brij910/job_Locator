package com.example.job_locator;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.CaseMap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class gmap extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    Location currentLocation;
    private static final int REQUEST_CODE=101;
    private ProgressDialog progressDialog;
    Button btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmap);
       // mMap.setMyLocationEnabled(true);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
       /* SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/
        btn =(Button)findViewById(R.id.btn);
        progressDialog =new ProgressDialog(gmap.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
        progressDialog.setCancelable(false);

        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();

   //     mMap.setMyLocationEnabled(true);

//        private void handleNewLocation(Location location){
//
//        }



    }

    private void fetchLastLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }


        Task<Location> task=fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location !=null){
                    currentLocation=location;
                 //   Toast.makeText(getApplicationContext(),currentLocation.getLatitude()+""+currentLocation.getLongitude(),Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
                    supportMapFragment.getMapAsync(gmap.this);
                }

            }
        });



    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        LatLng latLng1=new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        MarkerOptions mo=new MarkerOptions().position(latLng1).title("I am here").snippet("Current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng1));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng1,10));
        mMap.addMarker(mo);
       // mMap.setMyLocationEnabled(true);

        final ArrayList<String> loc = (ArrayList<String>)getIntent().getSerializableExtra("loc");
        final ArrayList<String> comp = (ArrayList<String>)getIntent().getSerializableExtra("comp");
        final ArrayList<String> tit = (ArrayList<String>)getIntent().getSerializableExtra("tit");
        final ArrayList<String> url = (ArrayList<String>)getIntent().getSerializableExtra("url");

       mMap.setInfoWindowAdapter(new CustomInfoW(gmap.this));
mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
    @Override
    public void onInfoWindowClick(Marker marker) {
        String s =  marker.getSnippet();
        Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(s));
        startActivity(viewIntent);
    }
});


       for(int i=0;i<loc.size();i++) {

               List<Address> addressList = new ArrayList<>();
               String pc = loc.get(i);
             String ur = url.get(i);
            String com = comp.get(i);
               if (pc != null || !pc.equals("")) {
                   Geocoder geocoder = new Geocoder(gmap.this);
                   try {
                       addressList = geocoder.getFromLocationName(pc, loc.size());
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
                   Address address = addressList.get(0);
                   LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                   mMap.addMarker(new MarkerOptions().position(latLng).title(com).snippet(ur));

               }
           }
       progressDialog.dismiss();
       }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE:
                if(grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    fetchLastLocation();
                }
                break;
        }
    }

}
