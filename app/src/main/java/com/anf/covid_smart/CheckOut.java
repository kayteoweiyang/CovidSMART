package com.anf.covid_smart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.skyfishjy.library.RippleBackground;

import org.json.JSONException;
import org.json.JSONObject;

public class CheckOut extends AppCompatActivity {

    IResponse mResponseCallback = null;
    APIService apiService;
    ImageView checkoutImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        initAPICallback();

        checkoutImg = findViewById(R.id.checkOutImage);
        checkoutImg.setOnClickListener(buttonsOnClickListener);
        final RippleBackground rippleBackground = findViewById(R.id.ripple1);
        checkoutImg.setColorFilter(Color.argb(0, 0,0,0));


        rippleBackground.startRippleAnimation();
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
                        finish();
                        return true;
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
    private View.OnClickListener buttonsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.checkOutImage:
                    checkoutImg.setColorFilter(null);
                    Toast.makeText(CheckOut.this, "Get Current Location", Toast.LENGTH_LONG).show();
                    checkoutLocation();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + v.getId());
            }
        }
    };
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
                Toast.makeText(CheckOut.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        };
    }

    private void checkoutLocation() {

        // Init a new api service instance
        apiService = new APIService(mResponseCallback, this);
        // Parameters need to be in JSON format
        JSONObject postData = new JSONObject();
        // Tag is to differentiate the response inside the callback method.
        apiService.postMethod("auth", "/user/checkout.php", postData);

    }
    private void responseSuccess(JSONObject response) {
        try {
            Boolean isSuccessful = response.getBoolean(("success"));
            if (isSuccessful) {
                Toast.makeText(CheckOut.this, response.getString(("message")), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(CheckOut.this, response.getString(("message")), Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}