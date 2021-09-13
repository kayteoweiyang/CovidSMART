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
                    case R.id.nav_home:
                        return true;
                    case R.id.nav_ci:
                        startActivity(new Intent(getApplicationContext(),CheckIn.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_logout:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
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
                    Toast.makeText(HomeActivity.this, "Register Vaccine", Toast.LENGTH_LONG).show();
                    break;
                case R.id.booktestingHome:
                    Toast.makeText(HomeActivity.this, "Booking COVID Test", Toast.LENGTH_LONG).show();
                    break;
                case R.id.checkinHome:
                    Toast.makeText(HomeActivity.this, "Check IN", Toast.LENGTH_LONG).show();
                    Intent checkinIntent = new Intent(HomeActivity.this, CheckIn.class);
                    startActivity(checkinIntent);
                    break;
                case R.id.checkoutHome:
                    Toast.makeText(HomeActivity.this, "Check OUT", Toast.LENGTH_LONG).show();
                    Intent checkoutIntent = new Intent(HomeActivity.this, CheckOut.class);
                    startActivity(checkoutIntent);
                    break;
                case R.id.nearmeHome:
                    Toast.makeText(HomeActivity.this, "Near Me", Toast.LENGTH_LONG).show();
                    break;
                case R.id.globalHome:
                    Toast.makeText(HomeActivity.this, "Globally", Toast.LENGTH_LONG).show();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + v.getId());
            }
        }
    };

}