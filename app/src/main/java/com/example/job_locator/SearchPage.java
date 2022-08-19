package com.example.job_locator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class SearchPage extends AppCompatActivity {
    ListView listview;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase1;
    FirebaseUser currentUser;
    DatabaseReference databaseReference1;
    ArrayList<String> list = new ArrayList<>();
    DrawerLayout dl;
    FirebaseAuth firebaseAuth;

    PreferenceActivity.Header h1;
    ActionBarDrawerToggle t;
    private ProgressDialog progressDialog;
    NavigationView nv;
   AutoCompleteTextView keyword;
    EditText locationf;

    ArrayList<HashMap<String, String>> contactList;
    ArrayList<HashMap<String, String>> searchResult;
     private static final String[] arrayList=new String[]{
             "java","cpp","android","cprog","net",".net",".sales","10th","12th","Academic","Account","Accountant","Actor","Admin","Administration","Admission","Asp.Net","Office","Analyst"," Technician",
             "Billing","Business","Business","Cashier","Chemical","Chief","Company","core","Data","Data","Design","Designer","Digital","Diploma","E-commerce","Engineer","Embedded","Enterprise","Export","Faculty","Fashion","Field","Financial","Fresher","Front","Game","Graphic","Graphics","HR","Home",
             "Industrial","iOS","IT","Lab","Lab","Laravel","Manager","Mechanical","Mobile","Network","Neurosurgery","Node","NURSING","Pharmacy","PHP","Physiotherapist","Python","QA","Quality","React","ReactJS","Sales","Senior","Software","Software","Technical","Trainee","Web","Website","WordPress"
     };
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);
        dl = (DrawerLayout)findViewById(R.id.drawerLayout);
        t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();
        firebaseAuth=FirebaseAuth.getInstance();
        currentUser=firebaseAuth.getCurrentUser();

        updateheader();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView)findViewById(R.id.nav_view);


        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                progressDialog =new ProgressDialog(SearchPage.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );
                progressDialog.setCancelable(false);



                if(id==R.id.home){
                    Intent i=new Intent(SearchPage.this,MainActivity.class);
                    progressDialog.dismiss();
                    startActivity(i);
                }
                else if(id==R.id.search){
                    Intent i=new Intent(SearchPage.this,Keyword.class);
                    progressDialog.dismiss();
                    startActivity(i);
                }
                else if(id==R.id.changepass){
                    Intent i=new Intent(SearchPage.this,changePassword.class);
                    progressDialog.dismiss();
                    startActivity(i);
                }
                else if(id==R.id.nav_logout){
                    Intent i=new Intent(SearchPage.this,MainActivity.class);
                    progressDialog.dismiss();
                    startActivity(i);
                }
                else if(id==R.id.nav_share){
                    progressDialog.dismiss();
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


                else if(id==R.id.SaveJob){searchResult.clear();
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
                                                  //  String D = snapshot.child("Details").getValue(String.class);
                                                    String pc = snapshot.child("pluse code").getValue(String.class);

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
                                                Toast.makeText(SearchPage.this, "not availabel", Toast.LENGTH_LONG).show();
                                                progressDialog.dismiss();}
                                            else {
                                                Intent i = new Intent(SearchPage.this,list.class);
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
                    Intent i=new Intent(SearchPage.this,LoginActivity.class);
                    startActivity(i);
                }
                else if (id==R.id.regi){
                    Intent i=new Intent(SearchPage.this,RegisterActivity.class);
                    startActivity(i);
                }

                return true;

            }
        });
        keyword = (AutoCompleteTextView) findViewById(R.id.keyword1);



      // ArrayList arrayList=new ArrayList<>();

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(SearchPage.this, android.R.layout.simple_dropdown_item_1line, arrayList);
        keyword.setAdapter(arrayAdapter);
        keyword.setThreshold(1);
        keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("beforeTextChanged", String.valueOf(s));
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("onTextChanged", String.valueOf(s));
            }
            @Override
            public void afterTextChanged(Editable s) {
                Log.d("afterTextChanged", String.valueOf(s));
            }
        });




        locationf = (EditText) findViewById(R.id.locationf1);
        final Button Button = (Button) findViewById(R.id.register1);
        searchResult = new ArrayList<>();
        Button.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {

                progressDialog =new ProgressDialog(SearchPage.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );
                progressDialog.setCancelable(false);
               /* keyword = (EditText) findViewById(R.id.keyword1);
                locationf = (EditText) findViewById(R.id.locationf1);*/
                if (keyword.getText().toString().equals("") || locationf.getText().toString().equals("")) {
                    Toast.makeText(SearchPage.this,
                            "Enter Details",
                            Toast.LENGTH_LONG)
                            .show();
                    progressDialog.dismiss();
                }
                else{
                    details();
                }
            }

        });




    }


    public void details(){

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("data");

        //listview = (ListView) findViewById(R.id.lv);

        //   final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        //  listview.setAdapter(arrayAdapter);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                   /* Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                    String l = data.get("Location").toString();
                    String U = data.get("Url").toString();
                    String C = data.get("CompanyName").toString();
                    String T = data.get("Title").toString();*/
                    String idj = snapshot.getKey();
                    String l = snapshot.child("Location").getValue(String.class);
                    String U = snapshot.child("Url").getValue(String.class);
                    String C = snapshot.child("CompanyName").getValue(String.class);
                    String T = snapshot.child("Title").getValue(String.class);
                   String pc = snapshot.child("pluse code").getValue(String.class);
                    if (C.contains(keyword.getText().toString().toLowerCase()) || T.contains(keyword.getText().toString().toLowerCase())) {


                        HashMap<String, String> contact = new HashMap<>();
                       contact.put("idj", idj);
                        contact.put("company", C);
                        contact.put("title", T);
                        contact.put("location", l);
                       contact.put("plus", pc);
                        contact.put("url", U);
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
                    Toast.makeText(SearchPage.this, "not availabel", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();}
                else {
                    Intent i = new Intent(SearchPage.this,list.class);
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
    public void onBackPressed(){
        Intent i=new Intent(SearchPage.this,LoginActivity.class);
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