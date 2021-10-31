package com.anf.covid_smart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StatusResult extends AppCompatActivity {

    String cvresult, vaccine;
    ImageView imgVaccine, imgResult;
    Button btnVaccine, btnCovid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_result);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            vaccine = extras.getString("vaccine");
            cvresult = extras.getString("cvresult");
        }

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
        }
        else
        {
            imgVaccine.setImageResource(R.drawable.ic_vaccine_status_not);
            btnVaccine.setVisibility(View.VISIBLE);
        }
        if(cvresult.equals("1"))
        {
            imgResult.setImageResource(R.drawable.ic_done);
        }
        else
        {
            imgResult.setImageResource(R.drawable.ic_vaccine_status_not);
            btnCovid.setVisibility(View.VISIBLE);
        }

    }
}