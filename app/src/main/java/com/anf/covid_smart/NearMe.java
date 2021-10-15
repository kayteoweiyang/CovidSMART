package com.anf.covid_smart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

import java.util.List;
import java.util.Locale;

public class NearMe extends AppCompatActivity implements LocationListener {
    public static final String MY_PREFS_NAME = "AUTH_TOKEN";

    IResponse mResponseCallback = null;
    APIService apiService;
    ImageView refresh;
    LocationManager locationManager;
    double getLat, getLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_me);

        initAPICallback();
        checkMyPermission();
        refresh = findViewById(R.id.refreshNearme);
        BottomNavigationView btmNavView = findViewById(R.id.bottom_navigation);
        btmNavView.setSelectedItemId(R.id.nav_home);

        refresh.setOnClickListener(buttonsOnClickListener);
        btmNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.nav_alert:
                        return true;
                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
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
        getLocation();
        //getLocationAffected();
    }

    private View.OnClickListener buttonsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.refreshNearme:
                    Toast.makeText(NearMe.this, String.valueOf(getLat), Toast.LENGTH_LONG).show();
                    getLocationAffected();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + v.getId());
            }
        }
    };

    private void checkMyPermission() {
        if (ContextCompat.checkSelfPermission(NearMe.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(NearMe.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, NearMe.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            Geocoder geocoder = new Geocoder(NearMe.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0);

            getLat = location.getLatitude();
            getLong = location.getLongitude();
            //addressTV.setText("Check in successfully at " + address);
            //latlongTV.setText("Latitude: "+location.getLatitude()+" , Longitude : "+location.getLongitude());
            //resultTV.setText("Check in Successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
    private void getLocationAffected()
    {
        // Init a new api service instance
        apiService = new APIService(mResponseCallback, this);

        JSONObject getData = new JSONObject();
        try {
            Log.i("lat", String.valueOf(getLat));
            Log.i("lat", String.valueOf(getLong));
            getData.put("lat", getLat);
            getData.put("lng", getLong);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Tag is to differentiate the response inside the callback method.
        apiService.getMethod("auth", "/cases/nearby.php", getData);
    }

    private void responseSuccess(JSONObject response) {
        try {
            Boolean isSuccessful = response.getBoolean(("success"));
            if (isSuccessful) {
                JSONObject caseinfo = response.getJSONObject("cases");
                Log.i("ci", caseinfo.getString("address"));
            }
            else {
                Toast.makeText(NearMe.this, response.getString(("message")), Toast.LENGTH_LONG).show();
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
                Toast.makeText(NearMe.this, "Something went wrong, please try again!", Toast.LENGTH_LONG).show();
            }
        };
    }

}