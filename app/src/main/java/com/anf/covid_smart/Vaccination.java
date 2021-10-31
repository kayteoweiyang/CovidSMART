package com.anf.covid_smart;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Vaccination extends AppCompatActivity {
    public static final String MY_PREFS_NAME = "AUTH_TOKEN";
    public static String authToken = "";
    IResponse mResponseCallback = null;
    APIService apiService;

    List<String> times;

    String selectedDate;
    CalendarView cv;
    Button btnregister;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccination);

        btnregister = findViewById(R.id.bookVaccine);
        btnregister.setOnClickListener(buttonsOnClickListener);

        initAPICallback();

        cv = findViewById(R.id.calendarVaccine);
        cv.setMinDate((new Date().getTime() + 1));

        times = Arrays.asList("10:00:00","10:30:00","11:00:00","11:30:00","13:00:00","13:30:00","14:00:00","14:30:00","15:00:00", "15:30:00","16:00:00","16:30:00", "17:00:00");
        spinner = findViewById(R.id.timeVaccination);

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

        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = year + "-" + month + "-" + dayOfMonth;
                Log.i("Date", selectedDate);
                checkTime();
            }
        });
    }
    private View.OnClickListener buttonsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bookVaccine:
                    Intent confirmationIntent = new Intent(Vaccination.this, ConfirmBooking.class);
                    confirmationIntent.putExtra("date", selectedDate);
                    confirmationIntent.putExtra("type", "Vaccination");
                    startActivity(confirmationIntent);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + v.getId());
            }
        }
    };

    private void checkTime() {
        // Init a new api service instance
        apiService = new APIService(mResponseCallback, this);

        // Tag is to differentiate the response inside the callback method.
        apiService.getMethod("auth", "/appointment/vaccination/unavailabledates.php");
    }

    private void responseSuccess(JSONObject response) {
        try {
            Boolean isSuccessful = response.getBoolean(("success"));
            if (isSuccessful) {
                ArrayList<String> ua = new ArrayList<>();
                JSONArray unavail = response.getJSONArray("unavailableDates");
                if(unavail.length() > 0)
                {
                    for(int i = 0; i < unavail.length(); i++)
                    {
                        JSONObject datetime = unavail.getJSONObject(i);
                        String date = datetime.getString("datetime").substring(0,9);
                        String time = datetime.getString("datetime").substring(11,18);
                        Log.i("Date-1", date);
                        Log.i("Time-1", time);
                    }
                }

                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.user_type_selected, times);
                adapter.setDropDownViewResource(R.layout.user_type_dropdown);
                spinner.setAdapter(adapter);

            } else {
                Toast.makeText(Vaccination.this, response.getString(("message")), Toast.LENGTH_LONG).show();
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
                Toast.makeText(Vaccination.this, "Something went wrong, please try again!", Toast.LENGTH_LONG).show();
            }
        };
    }
}