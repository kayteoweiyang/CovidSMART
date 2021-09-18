package com.anf.covid_smart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "AUTH_TOKEN";

    IResponse mResponseCallback = null;
    APIService apiService;
    TextView fname, lname, addr, email, mobile, ic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initAPICallback();

        fname = findViewById(R.id.fnameProfile);
        lname = findViewById(R.id.lnameProfile);
        addr = findViewById(R.id.addressProfile);
        email = findViewById(R.id.emailProfile);
        mobile = findViewById(R.id.mobileProfile);
        ic = findViewById(R.id.icProfile);

        BottomNavigationView btmNavView = findViewById(R.id.bottom_navigation);
        btmNavView.setSelectedItemId(R.id.nav_profile);
        setProfileInformation();
        btmNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){

                    case R.id.nav_home:
                        Intent homeintent = new Intent(getApplicationContext(), HomeActivity.class);
                        homeintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeintent);
                        return true;
                    case R.id.nav_alert:
                        return true;
                    case R.id.nav_logout:
                        Intent logout = new Intent(getApplicationContext(), MainActivity.class);
                        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(logout);
                }
                return false;
            }
        });
    }

    private void setProfileInformation() {
        // Init a new api service instance
        apiService = new APIService(mResponseCallback, this);

        // Tag is to differentiate the response inside the callback method.
        apiService.getMethod("auth", "/user/profile.php");
    }

    private void responseSuccess(JSONObject response) {
        try {
            Boolean isSuccessful = response.getBoolean(("success"));
            if (isSuccessful) {
                JSONObject userinfo = response.getJSONObject("user");
                fname.setText(userinfo.getString("first_name"));
                lname.setText(userinfo.getString("last_name"));
                ic.setText(userinfo.getString("nric"));
                email.setText(userinfo.getString("email"));
                mobile.setText(userinfo.getString("phone"));
                addr.setText(userinfo.getString("address"));
            }
            else {
                Toast.makeText(ProfileActivity.this, response.getString(("message")), Toast.LENGTH_LONG).show();
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
                Toast.makeText(ProfileActivity.this, "Something went wrong, please try again!", Toast.LENGTH_LONG).show();
            }
        };
    }
}