package com.anf.covid_smart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class OrgHomeActivity extends AppCompatActivity {

    Button checkVaccination, checkTest, generateProof, uploadResult, activeNational, activeInternational, visitHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_home);

        checkVaccination = findViewById(R.id.checkVaccineOrg);
        checkTest = findViewById(R.id.checkTestOrg);
        generateProof = findViewById(R.id.generateproofOrg);
        uploadResult = findViewById(R.id.uploadResultOrg);
        activeNational = findViewById(R.id.activeStatusOrg);
        activeInternational = findViewById(R.id.activeStatus2Org);
        visitHistory = findViewById(R.id.visitHistoryOrg);

        BottomNavigationView btmNavView = findViewById(R.id.bottom_navigation);
        btmNavView.setSelectedItemId(R.id.nav_home);

        checkVaccination.setOnClickListener(buttonsOnClickListener);
        checkTest.setOnClickListener(buttonsOnClickListener);
        generateProof.setOnClickListener(buttonsOnClickListener);
        uploadResult.setOnClickListener(buttonsOnClickListener);
        activeNational.setOnClickListener(buttonsOnClickListener);
        activeInternational.setOnClickListener(buttonsOnClickListener);
        visitHistory.setOnClickListener(buttonsOnClickListener);


        btmNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.nav_home:
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
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private View.OnClickListener buttonsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.checkVaccineOrg:
                    break;
                case R.id.checkTestOrg:
                    break;
                case R.id.generateproofOrg:
                    break;
                case R.id.uploadResultOrg:
                    break;
                case R.id.activeStatusOrg:
                    break;
                case R.id.activeStatus2Org:
                    Intent globalIntent = new Intent(OrgHomeActivity.this, GlobalActivityOrg.class);
                    startActivity(globalIntent);
                    break;
                case R.id.visitHistoryOrg:
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + v.getId());
            }
        }
    };
}