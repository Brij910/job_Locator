package com.example.job_locator;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Keyword extends AppCompatActivity {

    private String TAG = Keyword.class.getSimpleName();

    DrawerLayout dl;
    PreferenceActivity.Header h1;
    ActionBarDrawerToggle t;
    NavigationView nv;

    EditText keyword, locationf;

    // URL to get contacts JSON
    public static String url = "https://jobs.github.com/positions.json?";

    //Intent intent = new Intent(this, ResultPage.class);
    ArrayList<HashMap<String, String>> contactList;
    ArrayList<HashMap<String, String>> searchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword);


        dl = (DrawerLayout)findViewById(R.id.drawerLayout);
        t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView)findViewById(R.id.nav_view);

        contactList = new ArrayList<>();

        final Button Button = (Button) findViewById(R.id.register);

        Button.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {
                keyword = (EditText) findViewById(R.id.keyword);
                locationf = (EditText) findViewById(R.id.locationf);
                if (keyword.getText().toString().equals("") || locationf.getText().toString().equals("")) {
                    Toast.makeText(Keyword.this,
                            "Enter Details",
                            Toast.LENGTH_LONG)
                            .show();
                } else {
                    searchResult = new ArrayList<>();
                    for (HashMap<String, String> contact : contactList) {

                        if (contact.get("company").toString().toLowerCase().contains(keyword.getText().toString().toLowerCase()) || contact.get("title").toString().toLowerCase().contains(keyword.getText().toString().toLowerCase())) {
                            searchResult.add(contact);

                        }

                    }
                    if (searchResult.size() == 0)
                        Toast.makeText(Keyword.this, "not availabel", Toast.LENGTH_LONG).show();
                    else {
                        Intent i = new Intent(Keyword.this, list.class);
                        i.putExtra("searchResult", searchResult);
                        startActivity(i);
                    }

               /*   Intent i = new Intent(Keyword.this, list.class);
                    i.putExtra("keyword", keyword.getText().toString());
                    i.putExtra("locationf", locationf.getText().toString());
                    startActivityForResult(i, 500);*/
                }
            }
        });
        new GetContacts().execute();

    }



    public class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        public Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e("SomeString", "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONArray array = new JSONArray(jsonStr);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject c = (JSONObject) array.getJSONObject(i);

                        String id = c.getString("id");
                        String company = c.getString("company");
                        String title = c.getString("title");
                        String location = c.getString("location");
                        String description = c.getString("description");
                        String url = c.getString("url");



                        HashMap<String, String> contact = new HashMap<>();


                        contact.put("id", id);
                        contact.put("company", company);
                        contact.put("title", title);
                        contact.put("location", location);
                        contact.put("description", description);
                        contact.put("url",url);

                        // adding contact to contact list
                        contactList.add(contact);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

}