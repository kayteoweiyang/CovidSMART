package com.anf.covid_smart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConfirmBooking extends AppCompatActivity {
    public static final String MY_PREFS_NAME = "AUTH_TOKEN";
    public static String authToken = "";
    IResponse mResponseCallback = null;
    APIService apiService;


    TextView type, date, time, location;
    Button btnConfirm, btnBack;
    String selecteddate, selectedtype, selectedtime, selectedloc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);

        btnConfirm = findViewById(R.id.bookConfirm);
        btnBack = findViewById(R.id.backConfirm);
        type = findViewById(R.id.txtTypeConfirm);
        date = findViewById(R.id.txtDateConfirm);
        time = findViewById(R.id.txtTimeConfirm);
        location = findViewById(R.id.txtLocConfirm);

        initAPICallback();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            selecteddate = extras.getString("date");
            selectedtype = extras.getString("type");
            selectedtime = extras.getString("time");
            selectedloc = extras.getString("location");
        }

        type.setText(selectedtype);
        time.setText(selectedtime);
        date.setText(selecteddate);
        location.setText(selectedloc);

        btnConfirm.setOnClickListener(buttonsOnClickListener);
    }
    private View.OnClickListener buttonsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bookConfirm:
                    uploadBooking();
                    Intent bookedIntent = new Intent(ConfirmBooking.this, HomeActivity.class);
                    Toast.makeText(ConfirmBooking.this, "Successfully Booked", Toast.LENGTH_LONG).show();
                    bookedIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(bookedIntent);
                    finish();
                    break;
                case R.id.backConfirm:
                    onBackPressed();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + v.getId());
            }
        }
    };
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void uploadBooking() {
        // Init a new api service instance
        apiService = new APIService(mResponseCallback, this);

        JSONObject postData = new JSONObject();
        try {
            postData.put("datetime", selecteddate + " " + selectedtime);
            postData.put("clinicName", selectedloc);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(selectedtype.equalsIgnoreCase("Covidtest")) {
            // Tag is to differentiate the response inside the callback method.
            apiService.postMethod("auth", "/appointment/covidtest.php", postData);
        }
        else
        {
            apiService.postMethod("auth", "/appointment/vaccination.php", postData);
        }
    }

    private void responseSuccess(JSONObject response) {
        try {
            Boolean isSuccessful = response.getBoolean(("success"));
            if (isSuccessful) {
                Toast.makeText(ConfirmBooking.this, response.getString(("message")), Toast.LENGTH_LONG).show();
                Intent homeintent = new Intent(getApplicationContext(), HomeActivity.class);
                homeintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeintent);
                finish();
            } else {
                Toast.makeText(ConfirmBooking.this, response.getString(("message")), Toast.LENGTH_LONG).show();
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
                Toast.makeText(ConfirmBooking.this, "Something went wrong, please try again!", Toast.LENGTH_LONG).show();
            }
        };
    }
}