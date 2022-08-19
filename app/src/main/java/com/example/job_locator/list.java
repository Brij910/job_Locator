package com.example.job_locator;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.pusher.pushnotifications.PushNotifications;

import java.util.ArrayList;
import java.util.HashMap;

import eightbitlab.com.blurview.BlurView;

//import com.androidtutorialshub.JobSeeker.R;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;

public class list extends AppCompatActivity {
    DrawerLayout dl;
    ActionBarDrawerToggle t;
    private ProgressDialog progressDialog;
    NavigationView nv;
    ListView lv;
    FirebaseAuth fAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase1;
    DatabaseReference databaseReference1;


    //FirebaseFirestore fStore;
    String userId;
    private Object Location;
    DatabaseReference reference;
    ArrayList<String> loc;
    ArrayList<String> url;
    ArrayList<String> comp;
    ArrayList<String> tit;
    ArrayList<HashMap<String, String>> sR;
    ArrayList<HashMap<String, String>> searchResult;
    AppCompatImageView apti;
    BlurView aptb;





    //DocumentReference documentReference;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        updateheader();
        searchResult = new ArrayList<>();
        apti = (AppCompatImageView)findViewById(R.id.img);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
       // Button b1 = (Button)findViewById(R.id.bt1);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent in = new Intent(list.this,gmap.class);
                in.putExtra("loc",loc);
                in.putExtra("comp",comp);
                in.putExtra("tit",tit);
                in.putExtra("url",url);
                startActivity(in);

            }
        });



apti.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        Intent in = new Intent(list.this,gmap.class);
        in.putExtra("loc",loc);
        in.putExtra("comp",comp);
        in.putExtra("tit",tit);
        in.putExtra("url",url);
        startActivity(in);

    }
});

        dl = (DrawerLayout)findViewById(R.id.drawerLayout);
        t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView)findViewById(R.id.nav_view);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id==R.id.home){
                    Intent i=new Intent(list.this,MainActivity.class);
                    startActivity(i);
                }
                else if(id==R.id.search){
                    Intent i=new Intent(list.this,SearchPage.class);
                    startActivity(i);
                }

                else if(id==R.id.nav_logout){
                    Intent i=new Intent(list.this,MainActivity.class);
                    startActivity(i);
                }

                else if(id==R.id.SaveJob){

                    searchResult.clear();
                    FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
                    final String cuid = currentuser.getUid();

                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference().child("savejob");

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {


                                String uid = snapshot1.child("uid").getValue(String.class);

                                if (uid.equals(cuid)) {
                                    final String idj = snapshot1.child("idj").getValue(String.class);
                                    firebaseDatabase1 = FirebaseDatabase.getInstance();
                                    databaseReference1 = firebaseDatabase1.getReference().child("data");
                                    // String A = databaseReference1.getKey();
                                    //  Toast.makeText(SearchPage.this,A,Toast.LENGTH_LONG).show();
                                    String A = snapshot1.getKey();
                                    databaseReference1.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                                String A = snapshot.getKey();
                                                if(idj.equals(A)) {
                                                    String l = snapshot.child("Location").getValue(String.class);
                                                    String U = snapshot.child("Url").getValue(String.class);
                                                    String C = snapshot.child("CompanyName").getValue(String.class);
                                                    String T = snapshot.child("Title").getValue(String.class);
                                                    String pc = snapshot.child("pluse code").getValue(String.class);

                                                    //  String D = snapshot.child("Details").getValue(String.class);

                                                    //   if (C.contains(keyword.getText().toString().toLowerCase()) || T.contains(keyword.getText().toString().toLowerCase())) {


                                                    HashMap<String, String> contact = new HashMap<>();
                                                    contact.put("idj", idj);
                                                    contact.put("company", C);
                                                    contact.put("title", T);
                                                    contact.put("location", l);
                                                    //  contact.put("description", D);
                                                    contact.put("url", U);
                                                    contact.put("plus", pc);
                                                    // contactList.add(contact);
                                                    searchResult.add(contact);

                                                }
                                                // String data=snapshot.child("Title").getValue(String.class);
                                                //String data1=snapshot.child("Url").getValue(String.class);
                                                // list.add(data);
                                                //list.add(data1);
                                                //   arrayAdapter.notifyDataSetChanged();
                                            }
                                            if (searchResult.size() == 0){
                                                Toast.makeText(list.this, "not availabel", Toast.LENGTH_LONG).show();
                                                progressDialog.dismiss();}
                                            else {
                                                Intent i = new Intent(list.this,list.class);
                                                i.putExtra("searchResult", searchResult);
                                                startActivity(i);
                                            }

                                            //  String a = searchResult.toString();
                                            //  Toast.makeText(SearchPage.this,a,Toast.LENGTH_LONG).show();

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });



                                }





                            }

                        }



                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

/*  Intent i=new Intent(SearchPage.this,SavedJobActivity.class);
  startActivity(i);*/

                }

                else if(id==R.id.login){
                    Intent i=new Intent(list.this,LoginActivity.class);
                    startActivity(i);
                }
                else if(id==R.id.changepass){
                    Intent i=new Intent(list.this,changePassword.class);
                    startActivity(i);
                }
                else if (id==R.id.regi){
                    Intent i=new Intent(list.this,RegisterActivity.class);
                    startActivity(i);
                }
                else if(id==R.id.nav_share){
                    ApplicationInfo api = getApplicationContext().getApplicationInfo();
                    String apkpath = api.sourceDir;
                    Intent intent= new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    String a = "JOB_LOCATOR";
                    String b = "Your subject here";
                    //  intent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(new File(apkpath)));
                    intent.putExtra(Intent.EXTRA_TEXT,a);
                    intent.putExtra(Intent.EXTRA_SUBJECT,b);
                    startActivity(Intent.createChooser(intent,"ShareVia"));
                }


                return true;

            }
        });


        lv = (ListView)findViewById(R.id.list);
        //Bundle extras = getIntent().getExtras();
        lv.setAdapter(null);
        final ArrayList<HashMap<String, String>> searchResult = (ArrayList<HashMap<String, String>>)getIntent().getSerializableExtra("searchResult");
        final ListAdapter adapter = new SimpleAdapter(
                list.this, searchResult,
                R.layout.list_item, new String[]{"location", "company",
                "title", "url","idj","plus"}, new int[]{R.id.location,
                R.id.company, R.id.title, R.id.url,R.id.idj,R.id.pc});


        lv.setAdapter(adapter);


        loc = new ArrayList<String>();
        comp = new ArrayList<String>();
        tit = new ArrayList<String>();
        url = new ArrayList<String>();
        sR = new ArrayList<>();
        for (HashMap<String, String> result : searchResult) {

            String a = result.get("plus").toString();
            String b = result.get("company").toString();
            String c = result.get("title").toString();
            String u = result.get("url").toString();
            loc.add(a);
            comp.add(b);
            tit.add(c);
            url.add(u);


            //  Toast.makeText(list.this, a, Toast.LENGTH_LONG);
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {



                String l= ((TextView)view.findViewById(R.id.location)).getText().toString();
               String idj= ((TextView)view.findViewById(R.id.idj)).getText().toString();
                String c= ((TextView)view.findViewById(R.id.company)).getText().toString();
                String t= ((TextView)view.findViewById(R.id.title)).getText().toString();
                String u= ((TextView)view.findViewById(R.id.url)).getText().toString();
            // Intent intent = new Intent(list.this, data.class);
             //intent.putExtra("searchResult",searchResult.get(position));
                //Toast.makeText(list.this,idj,Toast.LENGTH_LONG).show();
                // intent.putExtra("location",a);
               //startActivity(intent);
               // String b = searchResult.get(position).toString();
                //  String d = searchResult.get(position).get(adapter url).toString();
                //   Toast.makeText(list.this,b,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(list.this, data2.class);
             //   intent.putExtra("description", b);
                intent.putExtra("location", l);
                intent.putExtra("company", c);
                intent.putExtra("title", t);
                intent.putExtra("url", u);
                intent.putExtra("idj",idj);
                startActivity(intent);

            }
        });


    }
    public void onBackPressed(){
        Intent i=new Intent(list.this,SearchPage.class);
        startActivity(i);
    }
    public void updateheader(){
        nv = (NavigationView)findViewById(R.id.nav_view);
        final View headerView=nv.getHeaderView(0);
        TextView navusername=headerView.findViewById(R.id.nav_username);
        FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
        final String cuid = currentuser.getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("user").child(cuid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nameheader=dataSnapshot.child("name").getValue(String.class);
                TextView navusername=headerView.findViewById(R.id.nav_username);
                navusername.setText("Hi,"+nameheader);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}