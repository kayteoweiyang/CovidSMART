package com.anf.covid_smart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    public static final String MY_PREFS_NAME = "AUTH_TOKEN";

    IResponse mResponseCallback = null;
    APIService apiService;
    Button registerBtn;
    TextView backtologin;
    EditText reglName, regfName, regEmail, regIC, regPw, regConfirmPw, regPhone, regAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initAPICallback();

        registerBtn = findViewById(R.id.registerbtnReg);
        backtologin = findViewById(R.id.tologinbtnReg);

        regfName = findViewById(R.id.firstnameReg);
        reglName = findViewById(R.id.lastnameReg);
        regEmail = findViewById(R.id.emailReg);
        regIC = findViewById(R.id.icReg);
        regPw = findViewById(R.id.passwordReg);
        regConfirmPw = findViewById(R.id.cpasswordReg);
        regAdd = findViewById(R.id.addressReg);
        regPhone = findViewById(R.id.phoneReg);

        registerBtn.setOnClickListener(buttonsOnClickListener);
        backtologin.setOnClickListener(buttonsOnClickListener);
    }

    private View.OnClickListener buttonsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.registerbtnReg:
                    Toast.makeText(RegisterActivity.this, "Registering", Toast.LENGTH_LONG).show();
                    checkRegister();

                    break;
                case R.id.tologinbtnReg:
                    Toast.makeText(RegisterActivity.this, "Back to Login", Toast.LENGTH_LONG).show();
                    Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + v.getId());
            }
        }
    };

    private void checkRegister() {
        String nric = regIC.getText().toString();
        String password = regPw.getText().toString();
        //String cpw = regConfirmPw.getText().toString();
        String fname = regfName.getText().toString();
        String lname = reglName.getText().toString();
        String addr = regAdd.getText().toString();
        String phone = regPhone.getText().toString();
        String email = regEmail.getText().toString();

        // Init a new api service instance
        apiService = new APIService(mResponseCallback, this);
        // Parameters need to be in JSON format
        JSONObject postData = new JSONObject();
        try {
            postData.put("nric", nric);
            postData.put("password", password);
            postData.put("firstName", fname);
            postData.put("lastName", lname);
            postData.put("email", email);
            postData.put("phone", phone);
            postData.put("address", addr);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Tag is to differentiate the response inside the callback method.
        apiService.postMethod("auth", "/authentication/register.php", postData);

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
                //Toast.makeText(RegisterActivity.this, "Something went wrong, please try again!", Toast.LENGTH_LONG).show();
                Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        };
    }

    private void responseSuccess(JSONObject response) {
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        try {
            Boolean isSuccessful = response.getBoolean(("success"));
            if (isSuccessful) {
                Toast.makeText(RegisterActivity.this, response.getString(("message")), Toast.LENGTH_LONG).show();
                startActivity(mainIntent);
            } else {
                Toast.makeText(RegisterActivity.this, response.getString(("message")), Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private Boolean validateName() {
        String val = reglName.getText().toString();
        if(val.isEmpty()) {
            reglName.setError("Field cannot be empty");
            return false;
        }
        else {
            reglName.setError(null);
            return true;
        }
    }
    private Boolean validateEmail() {
        String val = regEmail.getText().toString();
        if(val.isEmpty()) {
            regEmail.setError("Field cannot be empty");
            return false;
        }
        else {
            regEmail.setError(null);
            return true;
        }
    }
}