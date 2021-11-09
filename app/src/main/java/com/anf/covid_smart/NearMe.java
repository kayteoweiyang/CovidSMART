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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class NearMe extends AppCompatActivity implements LocationListener {
    public static final String MY_PREFS_NAME = "AUTH_TOKEN";

    IResponse mResponseCallback = null;
    APIService apiService;
    ImageView refresh;
    LocationManager locationManager;
    ListView listView;
    double getLat, getLong;
    String getAddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_me);

        initAPICallback();
        checkMyPermission();
        refresh = findViewById(R.id.refreshNearme);
        listView = findViewById(R.id.location_list);

        BottomNavigationView btmNavView = findViewById(R.id.bottom_navigation);
        btmNavView.setSelectedItemId(R.id.nav_home);

        refresh.setOnClickListener(buttonsOnClickListener);
        btmNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

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
        getLocation();
        getLocationAffected();
    }

    private View.OnClickListener buttonsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.refreshNearme:
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
            Location myLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0, NearMe.this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,0, NearMe.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            Geocoder geocoder = new Geocoder(NearMe.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            getLat = location.getLatitude();
            getLong = location.getLongitude();
            getAddr = addresses.get(0).getAddressLine(0);
            //locationManager.removeUpdates(this);

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

        String params = String.format("lat=%s&lng=%s", getLat, getLong);

        // Tag is to differentiate the response inside the callback method.
        apiService.getMethodwData("auth", "/cases/nearby.php", params);
    }

    private void responseSuccess(JSONObject response) {
        try {
            Boolean isSuccessful = response.getBoolean(("success"));
            if (isSuccessful) {
                ArrayList<String> addr = new ArrayList<String>();
                try {
                    JSONArray caseinfo = response.getJSONArray("nearbyCases");
                    for (int i = 0; i < caseinfo.length(); i++) {
                        JSONObject info = caseinfo.getJSONObject(i);
                        addr.add(info.getString("address"));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(NearMe.this, android.R.layout.simple_list_item_1, addr);
                    listView.setAdapter(adapter);
                }
                catch(JSONException e)
                {
                    Toast.makeText(NearMe.this, "No result found.", Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(NearMe.this, response.getString(("message")), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(NearMe.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        };
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

}