package com.anf.covid_smart;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    public static final String MY_PREFS_NAME = "AUTH_TOKEN";

    IResponse mResponseCallback = null;
    APIService apiService;
    Button loginBtn, registerBtn;
    EditText nricInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAPICallback();
        checkMyPermission();

        registerBtn = findViewById(R.id.registerbtnMain);
        loginBtn = findViewById(R.id.loginbtnMain);

        nricInput = (EditText) findViewById(R.id.nricMain);
        passwordInput = (EditText) findViewById(R.id.passwordMain);

        registerBtn.setOnClickListener(buttonsOnClickListener);
        loginBtn.setOnClickListener(buttonsOnClickListener);
    }

    private View.OnClickListener buttonsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.loginbtnMain:
                    login();
                    break;
                case R.id.registerbtnMain:
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("Attempting to register");
                    builder.setMessage("Register Complete");
                    builder.show();
                    //break;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + v.getId());
            }
        }
    };

    private void login() {
        String nric = nricInput.getText().toString();
        String password = passwordInput.getText().toString();

        // Init a new api service instance
        apiService = new APIService(mResponseCallback, this);

        // Parameters need to be in JSON format
        JSONObject postData = new JSONObject();
        try {
            postData.put("nric", nric);
            postData.put("password", password);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Tag is to differentiate the response inside the callback method.
        apiService.postMethod("auth","/authentication/login.php", postData);
    }

    private void loginSuccess(JSONObject response) {
        Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
        try {
            Boolean isSuccessful = response.getBoolean(("success"));
            if (isSuccessful) {
                String token = response.getString("token");
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("authToken", token);
                editor.apply();
                startActivity(homeIntent);
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
                        loginSuccess(response);
                }
            }

            @Override
            public void notifyError(String tag, VolleyError error) {
                Toast.makeText(MainActivity.this, "Something went wrong, please try again!", Toast.LENGTH_LONG).show();
            }
        };
    }
}