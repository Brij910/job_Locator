package com.example.job_locator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.widget.Button;

public class changePassword extends AppCompatActivity {
    EditText edtoldpass,edtnewpass,edtconfirmpass;
    Button passwordchnge;
    private  FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth fAuth;
    private  ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        edtoldpass=(EditText)findViewById(R.id.oldpass);
        edtnewpass=(EditText)findViewById(R.id.newpass);
        edtconfirmpass=(EditText)findViewById(R.id.confirmpass);

        passwordchnge=(Button) findViewById(R.id.buttonchange);
        passwordchnge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String oldPass,newPass,confirmpass;
                oldPass=edtoldpass.getText().toString();
                newPass=edtnewpass.getText().toString();
                confirmpass=edtconfirmpass.getText().toString();
                progressDialog =new ProgressDialog(changePassword.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );
                progressDialog.setCancelable(false);












                if(oldPass.equals("")){
                    Toast.makeText(changePassword.this,"enter old Password",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else if(newPass.equals("")){
                    Toast.makeText(changePassword.this,"enter new Password",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else if(oldPass.length()<6 || newPass.length()<6){
                    Toast.makeText(changePassword.this,"Password is short",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else if(oldPass.equals(newPass)){
                    Toast.makeText(changePassword.this,"Password is same",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else if(!newPass.equals(confirmpass)){
                    Toast.makeText(changePassword.this,"missmatch password!!!",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

               /* else if(!oldPass.equals()){
                    Toast.makeText(changePassword.this,"Password is same",Toast.LENGTH_SHORT).show();
                }*/
                else{



                    FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
                    final String cuid = currentuser.getUid();






                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference().child("user").child(cuid);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String pass=dataSnapshot.child("password").getValue(String.class);
                            if(!pass.equals(oldPass)){
                                Toast.makeText(changePassword.this,"enter correct old password",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                                AuthCredential credential= EmailAuthProvider.getCredential(user.getEmail(),oldPass);

                                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
                                                    //final String cuid = currentuser.getUid();


                                                    DatabaseReference update=FirebaseDatabase.getInstance().getReference("user").child(currentuser.getUid());
                                                    update.child("password").setValue(newPass);


                                                    Toast.makeText(changePassword.this,"Password changed",Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                    /*Intent i=new Intent(changePassword.this,LoginActivity.class);
                                                    startActivity(i);*/
                                                }else{
                                                    Toast.makeText(changePassword.this,"Password not changed",Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();

                                                }

                                            }
                                        });


                                    }
                                });

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });









                }

            }
        });
    }
}
