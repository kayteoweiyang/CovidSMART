package com.anf.covid_smart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class CovidTest extends AppCompatActivity {
    public static final String MY_PREFS_NAME = "AUTH_TOKEN";
    public static String authToken = "";
    IResponse mResponseCallback = null;
    APIService apiService;

    List<String> times;
    String selectedDate;
    CalendarView cvTest;
    Button btnregister;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_test);

        btnregister = findViewById(R.id.bookCVTest);
        btnregister.setOnClickListener(buttonsOnClickListener);

        cvTest = findViewById(R.id.calendarCVTest);
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

        cvTest.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = year + "/" + month + "/" + dayOfMonth;
                Log.i("Date", date);
            }
        });
    }
    private View.OnClickListener buttonsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bookVaccine:
                    Intent confirmationIntent = new Intent(CovidTest.this, ConfirmBooking.class);
                    startActivity(confirmationIntent);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + v.getId());
            }
        }
    };
}