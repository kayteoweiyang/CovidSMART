package com.anf.covid_smart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class ActiveStatus extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "AUTH_TOKEN";

    IResponse mResponseCallback = null;
    APIService apiService;

    TextView sgLoc, sgCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_status);

        initAPICallback();
        sgLoc = findViewById(R.id.sgLocOrg);
        sgCount = findViewById(R.id.sgCountOrg);

        BottomNavigationView btmNavView = findViewById(R.id.bottom_navigation);
        btmNavView.setSelectedItemId(R.id.nav_home);

        btmNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.nav_home:
                        Intent home = new Intent(getApplicationContext(), OrgHomeActivity.class);
                        home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(home);
                        finish();
                        return true;
                    case R.id.nav_alert:
                        Intent alert = new Intent(getApplicationContext(), AlertActivityOrg.class);
                        alert.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(alert);
                        finish();
                        return true;
                    case R.id.nav_logout:
                        Intent logout = new Intent(getApplicationContext(), Logout.class);
                        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(logout);
                        finish();
                        return true;
                }
                return false;
            }
        });
        getActiveStatus();
    }

    private void getActiveStatus() {
        // Init a new api service instance
        apiService = new APIService(mResponseCallback, this);

        // Tag is to differentiate the response inside the callback method.
        apiService.getMethod("auth", "/cases/all.php");
    }


    private void responseSuccess(JSONObject response) {
        try {
            Boolean isSuccessful = response.getBoolean(("success"));
            if (isSuccessful) {
                ArrayList<String> addr = new ArrayList<String>();
                ArrayList<Integer> count = new ArrayList<Integer>();
                JSONArray records = response.getJSONArray("allCases");
                for (int i = 0; i < records.length(); i++) {
                    JSONObject record = records.getJSONObject(i);
                    addr.add(record.getString("address"));
                    count.add(Integer.valueOf(record.getString("COUNT(address)")));
                }
                Integer num = 0;
                for(int i : count)
                {
                    num += i;
                }
                sgLoc.setText(String.valueOf(addr.size()));
                sgCount.setText(String.valueOf(num));
                //ArrayAdapter<String> adapter = new ArrayAdapter<String>(NearMe.this, android.R.layout.simple_list_item_1, addr);
                //listView.setAdapter(adapter);
            }
            else {
                Toast.makeText(ActiveStatus.this, response.getString(("message")), Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    // Callback method for api calls. Response will be inside here.
    void initAPICallback(){
        mResponseCallback = new IResponse() {
            @Override
            public void notifySuccess(String tag, JSONObject response) {
                switch (tag) {
                    case "auth":
                        responseSuccess(response);
                }
            }

            @Override
            public void notifyError(String tag, VolleyError error) {
                Log.i("error", error.toString());
                Toast.makeText(ActiveStatus.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        };
    }
}