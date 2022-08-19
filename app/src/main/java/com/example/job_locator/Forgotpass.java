package com.example.job_locator;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgotpass extends AppCompatActivity {


    EditText userEmail;
    Button userPass;

    FirebaseAuth firebaseAuth;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);
        userEmail = findViewById(R.id.textInputEditTextEmail);
        userPass = findViewById(R.id.fpass);

        firebaseAuth= FirebaseAuth.getInstance();
        userPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.sendPasswordResetEmail(userEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       if(task.isSuccessful()){

                           Toast.makeText(Forgotpass.this,"Password Send To Your Email",Toast.LENGTH_LONG).show();
                       }
                       else {
                           Toast.makeText(Forgotpass.this,"Email id does not exist",Toast.LENGTH_LONG).show();
                       }
                    }
                });
            }
        });
    }
}
