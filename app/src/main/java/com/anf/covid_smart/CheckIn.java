package com.anf.covid_smart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.skyfishjy.library.RippleBackground;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

public class CheckIn extends AppCompatActivity implements LocationListener {
    public static final String MY_PREFS_NAME = "AUTH_TOKEN";

    IResponse mResponseCallback = null;
    APIService apiService;
    ImageView checkinImg, searchImg;
    LocationManager locationManager;
    EditText postal;
    TextView addressTV;
    TextView latlongTV;
    TextView resultTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        initAPICallback();

        checkinImg = findViewById(R.id.checkInImage);
        searchImg = findViewById(R.id.searchCI);
        postal = findViewById(R.id.addressCI);
        addressTV = findViewById(R.id.textView);
        //latlongTV = findViewById(R.id.latlongCI);
        //resultTV = findViewById(R.id.resultCI);
        checkinImg.setOnClickListener(buttonsOnClickListener);

        final RippleBackground rippleBackground = findViewById(R.id.ripple);
        checkinImg.setColorFilter(Color.argb(0, 0,0,0));

        rippleBackground.startRippleAnimation();


        checkMyPermission();
        BottomNavigationView btmNavView = findViewById(R.id.bottom_navigation);
        btmNavView.setSelectedItemId(R.id.nav_home);

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
                    case R.id.nav_profile:
                        Intent profile = new Intent(getApplicationContext(), ProfileActivity.class);
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

    private View.OnClickListener buttonsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.checkInImage:
                    checkinImg.setColorFilter(null);
                    Toast.makeText(CheckIn.this, "Get Current Location", Toast.LENGTH_LONG).show();
                    getLocation();
                    break;
                case R.id.searchCI:
                    getaddress();
                default:
                    throw new IllegalStateException("Unexpected value: " + v.getId());
            }
        }
    };

    private void checkMyPermission() {
        if (ContextCompat.checkSelfPermission(CheckIn.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CheckIn.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }
    }

    private void getaddress() {
        String postalcode = postal.getText().toString();


    }
    @SuppressLint("MissingPermission")
    private void getLocation() {
        try
        {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5, CheckIn.this);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        try {
            Geocoder geocoder = new Geocoder(CheckIn.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String address = addresses.get(0).getAddressLine(0);

            checkinLocation(address, location.getLatitude(), location.getLongitude());
            addressTV.setText("Check in successfully at " + address);
            //latlongTV.setText("Latitude: "+location.getLatitude()+" , Longitude : "+location.getLongitude());
            //resultTV.setText("Check in Successfully");

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkinLocation(String addr, Double lat, Double lng) {

        // Init a new api service instance
        apiService = new APIService(mResponseCallback, this);
        // Parameters need to be in JSON format
        JSONObject postData = new JSONObject();
        try {
            postData.put("address", addr);
            postData.put("lat", lat);
            postData.put("lng", lng);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Tag is to differentiate the response inside the callback method.
        apiService.postMethod("auth", "/user/checkin.php", postData);

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
                //Toast.makeText(RegisterActivity.this, "Something went wrong, please try again!", Toast.LENGTH_LONG).show();
                Toast.makeText(CheckIn.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        };
    }

    private void responseSuccess(JSONObject response) {
        try {
            Boolean isSuccessful = response.getBoolean(("success"));
            if (isSuccessful) {
                Toast.makeText(CheckIn.this, response.getString(("message")), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(CheckIn.this, response.getString(("message")), Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}