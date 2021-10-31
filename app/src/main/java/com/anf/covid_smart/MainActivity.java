package com.anf.covid_smart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String MY_PREFS_NAME = "AUTH_TOKEN";
    public static String authToken = "";

    IResponse mResponseCallback = null;
    APIService apiService;
    Button loginBtn, registerBtn;
    EditText userInput, passwordInput;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAPICallback();
        checkMyPermission();

        registerBtn = findViewById(R.id.registerbtnMain);
        loginBtn = findViewById(R.id.loginbtnMain);

        userInput = (EditText) findViewById(R.id.nricUenMain);
        passwordInput = (EditText) findViewById(R.id.passwordMain);

        final List<String> types = Arrays.asList("Public User", "Healthcare User");
        spinner = findViewById(R.id.usertypeMain);
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.user_type_selected, types);
        adapter.setDropDownViewResource(R.layout.user_type_dropdown);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(spinner.getSelectedItemId() == 0) {
                    userInput.setHint("NRIC");
                    registerBtn.setEnabled(true);
                }
                else {
                    userInput.setHint("UEN");
                    registerBtn.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                userInput.setHint("NRIC");
            }
        });

        registerBtn.setOnClickListener(buttonsOnClickListener);
        loginBtn.setOnClickListener(buttonsOnClickListener);
    }

    int counter = 0;
    @Override
    public void onBackPressed() {
        counter++;
        if(counter == 1)
        {
            Toast.makeText(MainActivity.this, "Press back button twice to exit the app", Toast.LENGTH_LONG).show();
        }
        if(counter == 2)
        {
            super.onBackPressed();
        }
    }
    private View.OnClickListener buttonsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.loginbtnMain:
                    login();
                    break;
                case R.id.registerbtnMain:
                    Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(registerIntent);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + v.getId());
            }
        }
    };

    private void login() {
        String user = userInput.getText().toString();
        String password = passwordInput.getText().toString();
        Long ut = spinner.getSelectedItemId();
        if(ut == 0) {
            // Init a new api service instance
            apiService = new APIService(mResponseCallback, this);

            // Parameters need to be in JSON format
            JSONObject postData = new JSONObject();
            try {
                postData.put("nric", user);
                postData.put("password", password);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Tag is to differentiate the response inside the callback method.
            apiService.postMethod("auth", "/authentication/login.php", postData);
        }
        else
        {
            // Init a new api service instance
            apiService = new APIService(mResponseCallback, this);

            // Parameters need to be in JSON format
            JSONObject postData = new JSONObject();
            try {
                postData.put("uenNumber", user);
                postData.put("password", password);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Tag is to differentiate the response inside the callback method.
            apiService.postMethod("auth", "/admin/login.php", postData);
            //Intent orgIntent = new Intent(MainActivity.this, OrgHomeActivity.class);
            //startActivity(orgIntent);
        }
    }

    private void responseSuccess(JSONObject response) {
        Long ut = spinner.getSelectedItemId();
        Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
        Intent orgIntent = new Intent(MainActivity.this, OrgHomeActivity.class);
        try {
            Boolean isSuccessful = response.getBoolean(("success"));
            if (isSuccessful) {
                String token = response.getString("token");
                MainActivity.authToken = token;
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("authToken", token);
                editor.apply();
                if(ut == 0) {
                    startActivity(homeIntent);
                }
                else
                {
                    startActivity(orgIntent);
                }

            } else {
                Toast.makeText(MainActivity.this, response.getString(("message")), Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void checkMyPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }
    }
    // Callback method for api calls. Response will be inside here.
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
                Toast.makeText(MainActivity.this, "Something went wrong, please try again!", Toast.LENGTH_LONG).show();
            }
        };
    }

    private void redirectToHomePage() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}