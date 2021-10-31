package com.anf.covid_smart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CovidTest extends AppCompatActivity {
    public static final String MY_PREFS_NAME = "AUTH_TOKEN";
    public static String authToken = "";
    IResponse mResponseCallback = null;
    APIService apiService;

    ArrayList<String> times = new ArrayList<String>(12);
    String selectedDate;
    String selectedtime;
    String selectedloc;

    Boolean firstnode = false;

    CalendarView cvTest;
    Button btnregister;
    Spinner spinner;
    Spinner spinnerloc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_test);

        times.add("10:00:00");
        times.add("10:30:00");
        times.add("11:00:00");
        times.add("11:30:00");
        times.add("13:00:00");
        times.add("13:30:00");
        times.add("14:00:00");
        times.add("14:30:00");
        times.add("15:00:00");
        times.add("15:30:00");
        times.add("16:00:00");
        times.add("16:30:00");

        btnregister = findViewById(R.id.bookCVTest);
        btnregister.setOnClickListener(buttonsOnClickListener);

        initAPICallback();

        cvTest = findViewById(R.id.calendarCVTest);
        cvTest.setMinDate((new Date().getTime() + 1));
        cvTest.setEnabled(false);

        spinner = findViewById(R.id.timeCVTest);
        spinnerloc = findViewById(R.id.locCVTest);
        getClinics();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedtime = spinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        spinnerloc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedloc = spinnerloc.getSelectedItem().toString();
                cvTest.setEnabled(true);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

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
                selectedDate = year + "-" + String.format("%02d",month+1) + "-" + String.format("%02d",dayOfMonth);
                Log.i("Date", selectedDate);
                checkTime();
            }
        });
    }
    private View.OnClickListener buttonsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bookCVTest:
                    Intent confirmationIntent = new Intent(CovidTest.this, ConfirmBooking.class);
                    confirmationIntent.putExtra("date", selectedDate);
                    confirmationIntent.putExtra("type", "Covidtest");
                    confirmationIntent.putExtra("time", selectedtime);
                    confirmationIntent.putExtra("location", selectedloc);
                    startActivity(confirmationIntent);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + v.getId());
            }
        }
    };

    private void getClinics() {
        apiService = new APIService(mResponseCallback, this);
        apiService.getMethod("auth", "/appointment/clinics.php");
    }

    private void checkTime() {
        // Init a new api service instance
        apiService = new APIService(mResponseCallback, this);

        // Tag is to differentiate the response inside the callback method.
        apiService.getMethod("auth", "/appointment/covidtest/unavailabledates.php");
    }

    private void responseSuccess(JSONObject response) {
        try {
            Boolean isSuccessful = response.getBoolean(("success"));
            if (isSuccessful && firstnode == true) {
                ArrayList<String> new_time = new ArrayList<String>(12);
                for(int k = 0; k < times.size(); k++)
                {
                    new_time.add(times.get(k));
                }
                JSONArray unavail = response.getJSONArray("unavailableDates");
                if(unavail.length() > 0)
                {
                    for(int i = 0; i < unavail.length(); i++)
                    {
                        JSONObject datetime = unavail.getJSONObject(i);
                        String date = datetime.getString("datetime").substring(0,10);
                        String time = datetime.getString("datetime").substring(11,19);
                        if(selectedDate.equalsIgnoreCase(date))
                        {
                            int index = new_time.indexOf(time);
                            new_time.remove(index);
                        }
                    }
                }

                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.user_type_selected, new_time);
                adapter.setDropDownViewResource(R.layout.user_type_dropdown);
                spinner.setAdapter(adapter);
            }
            else if(isSuccessful && firstnode == false)
            {
                firstnode = true;
                ArrayList<String> clinic_list = new ArrayList<String>();
                JSONArray clinics = response.getJSONArray("clinics");
                for(int i = 0; i < clinics.length(); i++) {
                    JSONObject clinic = clinics.getJSONObject(i);
                    clinic_list.add(clinic.getString("name"));
                }
                ArrayAdapter adapter_loc = new ArrayAdapter(getApplicationContext(), R.layout.user_type_selected, clinic_list);
                adapter_loc.setDropDownViewResource(R.layout.user_type_dropdown);
                spinnerloc.setAdapter(adapter_loc);
            }
            else {
                Toast.makeText(CovidTest.this, response.getString(("message")), Toast.LENGTH_LONG).show();
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
                Toast.makeText(CovidTest.this, "Something went wrong, please try again!", Toast.LENGTH_LONG).show();
            }
        };
    }
}