package com.anf.covid_smart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "AUTH_TOKEN";
    public static String authToken = "";
    IResponse mResponseCallback = null;
    APIService apiService;

    TextView fname, lname, addr, email, mobile, ic;
    Button btnupdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initAPICallback();

        btnupdate = findViewById(R.id.updatebtnProfile);
        btnupdate.setOnClickListener(buttonsOnClickListener);

        fname = findViewById(R.id.fnameProfile);
        lname = findViewById(R.id.lnameProfile);
        addr = findViewById(R.id.addressProfile);
        email = findViewById(R.id.emailProfile);
        mobile = findViewById(R.id.mobileProfile);
        ic = findViewById(R.id.icProfile);

        BottomNavigationView btmNavView = findViewById(R.id.bottom_navigation);
        btmNavView.setSelectedItemId(R.id.nav_profile);
        getProfileInformation();
        btmNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){

                    case R.id.nav_home:
                        Intent homeintent = new Intent(getApplicationContext(), HomeActivity.class);
                        homeintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeintent);
                        finish();
                        return true;
                    case R.id.nav_alert:
                        Intent alertIntent = new Intent(getApplicationContext(), AlertActivity.class);
                        alertIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(alertIntent);
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
    }

    private View.OnClickListener buttonsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.updatebtnProfile:
                    Toast.makeText(ProfileActivity.this, "Update Profile", Toast.LENGTH_LONG).show();
                    updateProfile();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + v.getId());
            }
        }
    };
    private void getProfileInformation() {
        // Init a new api service instance
        apiService = new APIService(mResponseCallback, this);

        // Tag is to differentiate the response inside the callback method.
        apiService.getMethod("auth", "/user/profile.php");
    }

    private void updateProfile() {
        // Init a new api service instance
        apiService = new APIService(mResponseCallback, this);
        // Parameters need to be in JSON format
        JSONObject postData = new JSONObject();
        try {
            postData.put("firstName", fname.getText());
            postData.put("lastName", lname.getText());
            postData.put("email", email.getText());
            postData.put("phone", mobile.getText());
            postData.put("address", addr.getText());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Tag is to differentiate the response inside the callback method.
        apiService.putMethod("auth", "/user/profile.php", postData);

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
            
            if (response.getString("message") == "Profile updated successfully")
            {
                Toast.makeText(ProfileActivity.this, response.getString(("message")), Toast.LENGTH_LONG).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (response.getString("message").equalsIgnoreCase("Profile updated successfully")) {
                Toast.makeText(ProfileActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
            }
        }
        catch(JSONException e){

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