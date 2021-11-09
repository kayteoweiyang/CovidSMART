package com.anf.covid_smart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StatusResult extends AppCompatActivity {

    String cvresult, vaccine;
    ImageView imgVaccine, imgResult;
    Button btnVaccine, btnCovid;
    TextView infoResult, infoStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_result);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            vaccine = extras.getString("vaccine");
            cvresult = extras.getString("cvresult");
        }

        infoResult = findViewById(R.id.txtResult);
        infoStatus = findViewById(R.id.txtStatus);
        btnVaccine = findViewById(R.id.registervaccineStatus);
        btnCovid = findViewById(R.id.booktestingStatus);
        btnVaccine.setOnClickListener(buttonsOnClickListener);
        btnCovid.setOnClickListener(buttonsOnClickListener);

        imgVaccine = findViewById(R.id.imgVaccineStatus);
        imgResult = findViewById(R.id.imgResultStatus);

        setStatusInformation();

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
                case R.id.registervaccineHome:
                    Intent vaccineIntent = new Intent(StatusResult.this, Vaccination.class);
                    vaccineIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(vaccineIntent);
                    finish();
                    break;
                case R.id.booktestingHome:
                    Intent testIntent = new Intent(StatusResult.this, CovidTest.class);
                    testIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(testIntent);
                    finish();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + v.getId());
            }
        }
    };
    private void setStatusInformation() {
        if(vaccine.equals("1"))
        {
            imgVaccine.setImageResource(R.drawable.ic_done);
            infoStatus.setText("Vaccinated");
        }
        else
        {
            imgVaccine.setImageResource(R.drawable.ic_vaccine_status_not);
            infoStatus.setText("Not vaccinated, please book your free vaccination");
            btnVaccine.setVisibility(View.VISIBLE);
        }
        if(cvresult.equals("0"))
        {
            imgResult.setImageResource(R.drawable.ic_vaccine_status_not);
            btnCovid.setVisibility(View.VISIBLE);
            infoResult.setText("No COVID-19 test taken");
        }
        else if(cvresult.equals("1"))
        {
            imgResult.setImageResource(R.drawable.ic_baseline_health_and_safety_24);
            infoResult.setText("COVID-19 test result returns Negative");
        }
        else
        {
            imgResult.setImageResource(R.drawable.ic_coronavirus);
            btnCovid.setVisibility(View.VISIBLE);
            infoResult.setText("COVID-19 test result returns Positive. Please isolate yourself, we will contact you through SMS");
        }

    }
}