package com.anf.covid_smart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity{

    public static final String MY_PREFS_NAME = "AUTH_TOKEN";
    public static String authToken = "";
    IResponse mResponseCallback = null;
    APIService apiService;

    String u_vaccination, u_covid;

    Button registerbtn, bookingbtn, checkinbtn, checkoutbtn, nearmebtn, globalbtn;
    ImageView vaccine, test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initAPICallback();

        vaccine = findViewById(R.id.vaccineHome);
        test = findViewById(R.id.testHome);

        registerbtn = findViewById(R.id.registervaccineHome);
        bookingbtn = findViewById(R.id.booktestingHome);
        checkinbtn = findViewById(R.id.checkinHome);
        checkoutbtn = findViewById(R.id.checkoutHome);
        nearmebtn = findViewById(R.id.nearmeHome);
        globalbtn = findViewById(R.id.globalHome);

        BottomNavigationView btmNavView = findViewById(R.id.bottom_navigation);
        btmNavView.setSelectedItemId(R.id.nav_home);

        vaccine.setOnClickListener(buttonsOnClickListener);
        test.setOnClickListener(buttonsOnClickListener);
        registerbtn.setOnClickListener(buttonsOnClickListener);
        bookingbtn.setOnClickListener(buttonsOnClickListener);
        checkinbtn.setOnClickListener(buttonsOnClickListener);
        checkoutbtn.setOnClickListener(buttonsOnClickListener);
        nearmebtn.setOnClickListener(buttonsOnClickListener);
        globalbtn.setOnClickListener(buttonsOnClickListener);

        getProfileInformation();

        btmNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.nav_alert:
                        Intent alertAct = new Intent(getApplicationContext(), AlertActivity.class);
                        alertAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(alertAct);
                        finish();
                        return true;
                    case R.id.nav_profile:
                        Intent profile = new Intent(getApplicationContext(), ProfileActivity.class);
                        profile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(profile);
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
    @Override
    public void onBackPressed() {
            //super.onBackPressed();
    }
    private View.OnClickListener buttonsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.registervaccineHome:
                    Intent vaccineIntent = new Intent(HomeActivity.this, Vaccination.class);
                    startActivity(vaccineIntent);
                    break;
                case R.id.booktestingHome:
                    Intent testIntent = new Intent(HomeActivity.this, CovidTest.class);
                    startActivity(testIntent);
                    break;
                case R.id.checkinHome:
                    Intent checkinIntent = new Intent(HomeActivity.this, CheckIn.class);
                    startActivity(checkinIntent);
                    break;
                case R.id.checkoutHome:
                    Intent checkoutIntent = new Intent(HomeActivity.this, CheckOut.class);
                    startActivity(checkoutIntent);
                    break;
                case R.id.nearmeHome:
                    Intent nearmeIntent = new Intent(HomeActivity.this, NearMe.class);
                    startActivity(nearmeIntent);
                    break;
                case R.id.globalHome:
                    Intent globalIntent = new Intent(HomeActivity.this, GlobalActivity.class);
                    startActivity(globalIntent);
                    break;
                case R.id.vaccineHome:
                case R.id.testHome:
                    Intent statusIntent = new Intent(HomeActivity.this, StatusResult.class);
                    statusIntent.putExtra("vaccine", u_vaccination);
                    statusIntent.putExtra("cvresult", u_covid);
                    startActivity(statusIntent);
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
    private void responseSuccess(JSONObject response) {
        try {
            Boolean isSuccessful = response.getBoolean(("success"));
            if (isSuccessful) {
                JSONObject userinfo = response.getJSONObject("user");
                u_vaccination = userinfo.getString("vaccination_status");
                u_covid = userinfo.getString("covid_status");
                if(u_vaccination.equals("1"))
                {
                    vaccine.setImageResource(R.drawable.ic_done);
                }
                else
                {
                    vaccine.setImageResource(R.drawable.ic_vaccine_status_not);
                }
                if(u_covid.equals("0"))
                {
                    test.setImageResource(R.drawable.ic_vaccine_status_not);
                }
                else if(u_covid.equals("1"))
                {
                    test.setImageResource(R.drawable.ic_baseline_health_and_safety_24);
                }
                else
                {
                    test.setImageResource(R.drawable.ic_coronavirus);
                }
            }
            else {
                Toast.makeText(HomeActivity.this, response.getString(("message")), Toast.LENGTH_LONG).show();
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
                Toast.makeText(HomeActivity.this, "Something went wrong, please try again!", Toast.LENGTH_LONG).show();
            }
        };
    }

}