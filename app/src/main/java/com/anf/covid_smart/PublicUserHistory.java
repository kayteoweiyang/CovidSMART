package com.anf.covid_smart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PublicUserHistory extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "AUTH_TOKEN";

    IResponse mResponseCallback = null;
    APIService apiService;
    TextView et_nric, et_date;
    ImageView btn_search;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_user_history);

        initAPICallback();
        et_nric = findViewById(R.id.nricPUH);
        et_date = findViewById(R.id.datePUH);
        btn_search = findViewById(R.id.searchPUH);
        listView = findViewById(R.id.search_list);
        btn_search.setOnClickListener(buttonsOnClickListener);
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
    }
    private View.OnClickListener buttonsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.searchPUH:
                    if(et_nric.getText().length() == 9) {
                        Toast.makeText(PublicUserHistory.this, "Searching...", Toast.LENGTH_LONG).show();
                        getUserInfo();
                    }
                    else {
                        Toast.makeText(PublicUserHistory.this, "Please key in a valid NRIC", Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + v.getId());
            }
        }
    };
    private void getUserInfo() {
        // Init a new api service instance
        apiService = new APIService(mResponseCallback, this);

        String params = String.format("nric=%s&Date=%s", et_nric.getText(), et_date.getText());

        // Tag is to differentiate the response inside the callback method.
        apiService.getMethodwData("auth", "/admin/checkinrecords.php", params);
    }

    private void responseSuccess(JSONObject response) {
        try {
            Boolean isSuccessful = response.getBoolean(("success"));
            if (isSuccessful) {
                ArrayList<Records> rec = new ArrayList<>();
                JSONArray records = response.getJSONArray("records");
                if(records.length() < 0) {
                    for (int i = 0; i < records.length(); i++) {
                        JSONObject record = records.getJSONObject(i);
                        String A = record.getString("address");
                        String D = record.getString("DATE(datetime)");
                        String T = record.getString("TIME(datetime)");
                        rec.add(new Records(A, D, T));


                        //JSONObject info = records.getJSONObject(i);
                        //addr.add(info.getString("address"));
                    }
                    RecordAdapter recordAdapter = new RecordAdapter(PublicUserHistory.this, R.layout.records_case, rec);
                    listView.setAdapter(recordAdapter);
                }
                else {
                    Toast.makeText(PublicUserHistory.this, "No result found.", Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(PublicUserHistory.this, response.getString(("message")), Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
                Log.i("error", error.toString());
                Toast.makeText(PublicUserHistory.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        };
    }
}