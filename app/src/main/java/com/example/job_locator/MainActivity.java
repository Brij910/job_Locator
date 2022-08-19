package com.example.job_locator;

//import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//import com.androidtutorialshub.JobLocator.R;

public class MainActivity extends AppCompatActivity   {
    DrawerLayout drawer;
    ActionBarDrawerToggle drawertoggle;
    //NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        final Button Button = (Button) findViewById(R.id.loginbtn);
        final Button Button2 = (Button) findViewById(R.id.registrationbtn);
        //final android.widget.Button changeButton = (Button)findViewById(R.id.layoutButton);
        Button.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {
                openActivity2();
            }
        });
        Button2.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {
                openActivity3();
            }
        });

    }

    public void openActivity2() {

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void openActivity3() {

        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}

