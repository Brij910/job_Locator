package com.example.job_locator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//import com.androidtutorialshub.JobSeeker.R;
//import com.androidtutorialshub.JobSeeker.helpers.InputValidation;
//import com.androidtutorialshub.JobSeeker.model.User;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.FirebaseFirestore;


public class RegisterActivity extends AppCompatActivity  {

    private final AppCompatActivity activity = RegisterActivity.this;

    private NestedScrollView nestedScrollView;
    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;
    private TextInputLayout textInputLayoutContact;
    private TextInputEditText textInputEditTexContact;
    private TextInputEditText textInputEditTextName;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextConfirmPassword;
    private FirebaseAuth fAuth;

    private AppCompatButton appCompatButtonRegister;
    private AppCompatTextView appCompatTextViewLoginLink;
    private DatabaseReference databaseReference;


    //private InputValidation inputValidation;
    // private DatabaseHelper databaseHelper;
    //private User user;
    //private FirebaseFirestore mFirestore;
    private String userID;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
       // mFirestore=FirebaseFirestore.getInstance();
        fAuth=FirebaseAuth.getInstance();

        getSupportActionBar().setTitle("REGISTRATION PAGE");
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutName);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutContact=(TextInputLayout) findViewById(R.id.textInputLayoutContact);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.textInputLayoutConfirmPassword);

        textInputEditTextName = (TextInputEditText) findViewById(R.id.textInputEditTextName);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        textInputEditTextConfirmPassword = (TextInputEditText) findViewById(R.id.textInputEditTextConfirmPassword);
        textInputEditTexContact=(TextInputEditText) findViewById(R.id.textInputEditTextContact) ;

        appCompatButtonRegister = (AppCompatButton) findViewById(R.id.appCompatButtonRegister);
        databaseReference= FirebaseDatabase.getInstance().getReference("user");



        appCompatTextViewLoginLink = (AppCompatTextView) findViewById(R.id.appCompatTextViewLoginLink);
        appCompatTextViewLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegister = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentRegister);
            }
        });
        appCompatButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Name=textInputEditTextName.getText().toString();
                final String Email=textInputEditTextEmail.getText().toString();
                final String Password=textInputEditTextPassword.getText().toString();
                final String Contact=textInputEditTexContact.getText().toString();
                String ConfirmPassword=textInputEditTextConfirmPassword.getText().toString();


                if(TextUtils.isEmpty(Name)){
                    textInputEditTextName.setError("Name is Requires");
                    return;
                }
                if(TextUtils.isEmpty(Email)){
                    textInputEditTextEmail.setError("Email is Requires");
                    return;
                }
                if(TextUtils.isEmpty(Contact)){
                    textInputEditTexContact.setError("Contact is Requires");
                    return;
                }
                if(Contact.length()!=10){
                    textInputEditTexContact.setError("Contact must have 10 digit ");
                    return;
                }

                if(TextUtils.isEmpty(Password)){
                    textInputEditTextPassword.setError("Name is Requires");
                    return;
                }
                if(Password.length()<6){
                    textInputEditTextPassword.setError("password must be > 6 character..");
                    return;
                }
                if(!Password.equals(ConfirmPassword)){
                    textInputEditTextConfirmPassword.setError("Password is incorrect");
                    return;
                }
                fAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            final User r=new User(
                                    Name,Email,Contact,Password
                            );
                            /*databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    databaseReference.child("user01").setValue(r);
                                    Toast.makeText(RegisterActivity.this,"User Created", Toast.LENGTH_LONG).show();


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });*/


                            FirebaseDatabase.getInstance().getReference("user")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(r).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(RegisterActivity.this,"User Created", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));

                                }
                            });

                        }
                        else{
                            Toast.makeText(RegisterActivity.this,"error!"+task.getException().getMessage(), Toast.LENGTH_LONG).show();


                        }

                    }
                });





            }
        });
    }




}








