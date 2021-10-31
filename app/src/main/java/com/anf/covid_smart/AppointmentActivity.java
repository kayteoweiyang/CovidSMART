package com.anf.covid_smart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AppointmentActivity extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "AUTH_TOKEN";
    public static String authToken = "";
    IResponse mResponseCallback = null;
    APIService apiService;

    String selectedtype;
    TextView header;
    ListView listview;

    ArrayList<Booking> bookingList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            selectedtype = extras.getString("type");
        }
        header = findViewById(R.id.headerAppt);
        header.setText(selectedtype + " Appointment");
        listview = findViewById(R.id.appointments_org);

        initAPICallback();
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
        getAppointments();
    }

    private void getAppointments() {
        // Init a new api service instance
        apiService = new APIService(mResponseCallback, this);

        if(selectedtype.equalsIgnoreCase("Vaccination")) {
            apiService.getMethod("auth", "/admin/appointment/vaccination.php");
        }
        else
        {
            apiService.getMethod("auth", "/admin/appointment/covidtest.php");
        }
    }
    private void responseSuccess(JSONObject response) {
        try {
            Boolean isSuccessful = response.getBoolean(("success"));
            if (isSuccessful) {
                JSONArray b_array = response.getJSONArray("bookings");
                if(b_array.length() > 0)
                {
                    for(int i = 0; i < b_array.length(); i++)
                    {
                        JSONObject booking = b_array.getJSONObject(i);
                        String nric = booking.getString("nric");
                        String name = booking.getString("first_name") + " " + booking.getString("last_name");
                        String date = booking.getString("datetime").substring(0,10);
                        String time = booking.getString("datetime").substring(11,19);
                        Log.i("nric", nric);
                        Log.i("name", name);
                        Log.i("date", date);
                        Log.i("time", time);

                        bookingList.add(new Booking(name, nric,date,time));
                    }
                    BookingAdapter bookingAdapter = new BookingAdapter(AppointmentActivity.this, R.layout.booking_records, bookingList);
                    listview.setAdapter(bookingAdapter);
                }
            }
            else {
                Toast.makeText(AppointmentActivity.this, response.getString(("message")), Toast.LENGTH_LONG).show();
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
                Toast.makeText(AppointmentActivity.this, "Something went wrong, please try again!", Toast.LENGTH_LONG).show();
            }
        };
    }


}