package com.example.job_locator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
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

import java.util.ArrayList;
import java.util.HashMap;

public class data2 extends AppCompatActivity {
    DrawerLayout dl;
    ActionBarDrawerToggle t;
    private ProgressDialog progressDialog;
    NavigationView nv;
    private  FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference1;
    private FirebaseAuth fAuth;
    ArrayList<HashMap<String, String>> searchResult;
    FirebaseDatabase firebaseDatabase1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data2);
        fAuth=FirebaseAuth.getInstance();
        updateheader();
        searchResult = new ArrayList<>();
        databaseReference1= FirebaseDatabase.getInstance().getReference("savejob");

        dl = (DrawerLayout)findViewById(R.id.drawerLayout3);
        t = new ActionBarDrawerToggle(data2.this, dl,R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        nv = (NavigationView)findViewById(R.id.nav_view);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id==R.id.home){
                    Intent i=new Intent(data2.this,MainActivity.class);
                    startActivity(i);
                }
                else if(id==R.id.search){
                    Intent i=new Intent(data2.this,SearchPage.class);
                    startActivity(i);
                }

                else if(id==R.id.nav_logout){
                    Intent i=new Intent(data2.this,MainActivity.class);
                    startActivity(i);
                }

                else if(id==R.id.login){
                    Intent i=new Intent(data2.this,LoginActivity.class);
                    startActivity(i);
                }
                else if(id==R.id.changepass){
                    Intent i=new Intent(data2.this,changePassword.class);
                    startActivity(i);
                }
                else if (id==R.id.regi){
                    Intent i=new Intent(data2.this,RegisterActivity.class);
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
                                                Toast.makeText(data2.this, "not availabel", Toast.LENGTH_LONG).show();
                                                progressDialog.dismiss();}
                                            else {
                                                Intent i = new Intent(data2.this,list.class);
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
      /*  final ArrayList<HashMap<String, String>> searchResult = (ArrayList<HashMap<String, String>>)getIntent().getSerializableExtra("searchResult");
        String a= searchResult.get(0).toString();
        Toast.makeText(data.this,a,Toast.LENGTH_LONG).show();*/
        Bundle extras = getIntent().getExtras();
        //Intent intent = getIntent();
        final String idj = extras.getString("idj");
        final String location = extras.getString("location");
        String company = extras.getString("company");
        String title = extras.getString("title");
     /*   String description = extras.getString("description");
        description = description.replaceAll("\\<.*?\\>","");
        description = description.replaceAll("\\}","");
        description = description.replaceAll("\\{","");
        description = description.replaceAll("description=","");*/
        final String url = extras.getString("url");


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("data").child(idj);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              String kSkill=dataSnapshot.child("KeySkills").getValue().toString();
              String exp=dataSnapshot.child("Experience").getValue().toString();

                TextView tv6 = (TextView)findViewById(R.id.keyskills);
                tv6.setText(kSkill);
                TextView tv7 = (TextView)findViewById(R.id.exp);
                tv7.setText(exp);
                String description =dataSnapshot.child("Details").getValue().toString();
                description = description.replaceAll("\\<.*?\\>","");
                description = description.replaceAll("\\}","");
                description = description.replaceAll("\\{","");
                description = description.replaceAll("description=","");
              //  description = description.replaceAll("Responsibilities","\n \n\bResponsibilities:");
             //   description = description.replaceAll(".",".\n");
            //  String[] d = description.split("\\.");
                TextView tv4 = (TextView)findViewById(R.id.description);

                tv4.setText(description);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        TextView tv1 = (TextView)findViewById(R.id.location);
        tv1.setText(location);
        TextView tv2 = (TextView)findViewById(R.id.company);
        tv2.setText(company);
        TextView tv3 = (TextView)findViewById(R.id.title);
        tv3.setText(title);

        TextView tv5 = (TextView)findViewById(R.id.lname1);
        tv5.setText(company);

        AppCompatButton fab = (AppCompatButton)findViewById(R.id.apply);
        FloatingActionButton save = (FloatingActionButton)findViewById(R.id.Save);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(url));
                startActivity(viewIntent);

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* Toast.makeText(data2.this,"Job is saved",Toast.LENGTH_LONG).show();
                String id = databaseReference.push().getKey();*/

               // Save save = new Save(id);
                FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
                final String uid = currentuser.getUid();
                //Toast.makeText(data2.this,""+ uid+idj,Toast.LENGTH_LONG).show();
              /* databaseReference1.orderByChild("uid").equalTo(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       if(dataSnapshot.exists()){
                           String a = dataSnapshot.child("idj").getValue(String.class);
                           if(a.equals(idj)){
                               Toast.makeText(data2.this,"Already",Toast.LENGTH_LONG).show();
                           }
                       }
                       else{
                           String id = databaseReference1.push().getKey();
                           SaveJob saveJob = new SaveJob(id, idj, uid);
                           databaseReference1.child(id).setValue(saveJob);
                           Toast.makeText(data2.this,"Job is saved",Toast.LENGTH_LONG).show();
                       }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/


               String id = databaseReference1.push().getKey();
                SaveJob saveJob = new SaveJob(id, idj, uid);
                databaseReference1.child(id).setValue(saveJob);
                Toast.makeText(data2.this,"Job is saved",Toast.LENGTH_LONG).show();
            }
        });
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

