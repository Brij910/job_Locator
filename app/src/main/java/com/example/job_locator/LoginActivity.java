package com.example.job_locator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

//import com.androidtutorialshub.JobSeeker.R;
//import com.androidtutorialshub.JobSeeker.helpers.InputValidation;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity  {
    private final AppCompatActivity activity = LoginActivity.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;


    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;

    private AppCompatButton appCompatButtonLogin;
    private Button forgotpass;

    private AppCompatTextView textViewLinkRegister;
    private ProgressDialog progressDialog;

   // private InputValidation inputValidation;
    private FirebaseAuth firebaseAuth;
    // private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getSupportActionBar().setTitle("LOGIN PAGE");
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutContact);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);

        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextContact);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);

        appCompatButtonLogin = (AppCompatButton) findViewById(R.id.appCompatButtonLogin);
        forgotpass = (Button) findViewById(R.id.forgotpassword);

        textViewLinkRegister = (AppCompatTextView) findViewById(R.id.textViewLinkRegister);
        textViewLinkRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
            }
        });

        firebaseAuth=FirebaseAuth.getInstance();
        appCompatButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email=textInputEditTextEmail.getText().toString();
                String Password=textInputEditTextPassword.getText().toString();
                progressDialog =new ProgressDialog(LoginActivity.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );
                progressDialog.setCancelable(false);


                if(TextUtils.isEmpty((Email))){
                    textInputEditTextEmail.setError("Email is Requires");
                    progressDialog.dismiss();
                    return;
                }
                if(TextUtils.isEmpty(Password)){
                    textInputEditTextPassword.setError("Password is Requires");
                    progressDialog.dismiss();
                    return;
                }
                if(Password.length()<6){
                    textInputEditTextPassword.setError("password must be > 6 character..");
                    progressDialog.dismiss();
                    return;
                }
                firebaseAuth.signInWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intentRegister = new Intent(getApplicationContext(), SearchPage.class);
                                    startActivity(intentRegister);

                                } else {
                                    Toast.makeText(LoginActivity.this,"Login failed..!! Try Again..",Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();

                                }

                                // ...
                            }
                        });


            }
        });


        //initViews();
        //initListeners();
        //initObjects();
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(LoginActivity.this,Forgotpass.class);
                startActivity(in);
            }
        });
    }



/*


    private void initListeners() {
        appCompatButtonLogin.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);
    }


    private void initObjects() {
       // databaseHelper = new DatabaseHelper(activity);
        inputValidation = new InputValidation(activity);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonLogin:
               // verifyFromSQLite();
                break;
            case R.id.textViewLinkRegister:


                break;
        }
    }

/*
    private void verifyFromSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_email))) {
            return;
        }

       if (databaseHelper.checkUser(textInputEditTextEmail.getText().toString().trim()
                , textInputEditTextPassword.getText().toString().trim())) {


            Intent accountsIntent = new Intent(this, Keyword.class);
            //accountsIntent.putExtra("EMAIL", textInputEditTextEmail.getText().toString().trim());
            //emptyInputEditText();
            startActivity(accountsIntent);


        } else {

            Snackbar.make(nestedScrollView, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
        }
    }


    private void emptyInputEditText() {
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
    }*/
public void onBackPressed(){
    Intent i=new Intent(LoginActivity.this,MainActivity.class);
    startActivity(i);
}
}
