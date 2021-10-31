package com.anf.covid_smart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class VaccineCert extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "AUTH_TOKEN";
    public static String authToken = "";
    IResponse mResponseCallback = null;
    APIService apiService;

    TextView tvNRIC;
    CheckBox cbConfirm;
    Button btnGenerate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccine_cert);

        initAPICallback();

        tvNRIC = findViewById(R.id.icCert);
        btnGenerate = findViewById(R.id.generatebtnCert);
        btnGenerate.setOnClickListener(buttonsOnClickListener);
        cbConfirm = findViewById(R.id.cbConfirmCert);

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
    }
    private View.OnClickListener buttonsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.generatebtnCert:
                    if(tvNRIC.getText() != "" && cbConfirm.isChecked()) {
                        Toast.makeText(VaccineCert.this, "Generating...", Toast.LENGTH_SHORT).show();
                        generatePOC();
                        tvNRIC.setText("");
                        cbConfirm.setChecked(false);
                    }
                    else
                    {
                        Toast.makeText(VaccineCert.this, "Please key in a valid NRIC and checked the confirmation", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + v.getId());
            }
        }
    };

    private void generatePOC() {
        // Init a new api service instance
        apiService = new APIService(mResponseCallback, this);

        // Parameters need to be in JSON format
        JSONObject postData = new JSONObject();
        try {
            postData.put("vaccinationStatus", "1");
            postData.put("nric", tvNRIC.getText());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        apiService.putMethod("auth", "/admin/appointment/vaccination.php", postData);
    }
    private void responseSuccess(JSONObject response) {
        try {
            Boolean isSuccessful = response.getBoolean(("success"));
            if (isSuccessful) {
                Toast.makeText(VaccineCert.this, response.getString(("message")), Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(VaccineCert.this, response.getString(("message")), Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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
                Toast.makeText(VaccineCert.this, "Something went wrong, please try again!", Toast.LENGTH_LONG).show();
            }
        };
    }
}