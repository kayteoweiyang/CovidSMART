package com.anf.covid_smart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity{

    Button registerbtn, bookingbtn, checkinbtn, checkoutbtn, nearmebtn, globalbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        registerbtn = findViewById(R.id.registervaccineHome);
        bookingbtn = findViewById(R.id.booktestingHome);
        checkinbtn = findViewById(R.id.checkinHome);
        checkoutbtn = findViewById(R.id.checkoutHome);
        nearmebtn = findViewById(R.id.nearmeHome);
        globalbtn = findViewById(R.id.globalHome);

        BottomNavigationView btmNavView = findViewById(R.id.bottom_navigation);
        btmNavView.setSelectedItemId(R.id.nav_home);

        registerbtn.setOnClickListener(buttonsOnClickListener);
        bookingbtn.setOnClickListener(buttonsOnClickListener);
        checkinbtn.setOnClickListener(buttonsOnClickListener);
        checkoutbtn.setOnClickListener(buttonsOnClickListener);
        nearmebtn.setOnClickListener(buttonsOnClickListener);
        globalbtn.setOnClickListener(buttonsOnClickListener);


        btmNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.nav_alert:
                        return true;
                    case R.id.nav_profile:
                        Intent profileAct = new Intent(HomeActivity.this, ProfileActivity.class);
                        startActivity(profileAct);
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
                    //Toast.makeText(HomeActivity.this, "Register Vaccine", Toast.LENGTH_LONG).show();
                    break;
                case R.id.booktestingHome:
                    //Toast.makeText(HomeActivity.this, "Booking COVID Test", Toast.LENGTH_LONG).show();
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
                    //Toast.makeText(HomeActivity.this, "Near Me", Toast.LENGTH_LONG).show();
                    break;
                case R.id.globalHome:
                    Intent globalIntent = new Intent(HomeActivity.this, GlobalActivity.class);
                    startActivity(globalIntent);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + v.getId());
            }
        }
    };

}