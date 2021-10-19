package com.anf.covid_smart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class ActiveStatus extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "AUTH_TOKEN";

    IResponse mResponseCallback = null;
    APIService apiService;
    ListView listView;
    TextView sgLoc, sgCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_status);

        initAPICallback();
        sgLoc = findViewById(R.id.sgLocOrg);
        sgCount = findViewById(R.id.sgCountOrg);
        listView = findViewById(R.id.as_list);

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
        getActiveStatus();
    }

    private void getActiveStatus() {
        // Init a new api service instance
        apiService = new APIService(mResponseCallback, this);

        // Tag is to differentiate the response inside the callback method.
        apiService.getMethod("auth", "/cases/all.php");
    }


    private void responseSuccess(JSONObject response) {
        try {
            Boolean isSuccessful = response.getBoolean(("success"));
            if (isSuccessful) {
                List<Map<String, String>> data = new ArrayList<Map<String, String>>();
                //ArrayList<String> addr = new ArrayList<String>();
                ArrayList<String> count = new ArrayList<String>();
                JSONArray records = response.getJSONArray("allCases");
                for (int i = 0; i < records.length(); i++) {
                    JSONObject record = records.getJSONObject(i);
                    Log.i("record",record.getString("address") );
                    Map<String, String> datum = new HashMap<String, String>(2);
                    datum.put("Address", "Address : " + record.getString("address"));
                    datum.put("Count", "Number of Cases : " + record.getString("COUNT(address)"));
                    data.add(datum);
                    //addr.add(record.getString("address"));
                    count.add(record.getString("COUNT(address)"));
                }
                Integer num = 0;
                for(String i : count)
                {
                    num += Integer.valueOf(i);
                }
                sgLoc.setText(String.valueOf(data.size()));
                sgCount.setText(String.valueOf(num));

                SimpleAdapter adapter = new SimpleAdapter(ActiveStatus.this, data, android.R.layout.two_line_list_item, new String[] {"Address", "Count" },
                        new int[] {android.R.id.text1, android.R.id.text2 });
                listView.setAdapter(adapter);
            }
            else {
                Toast.makeText(ActiveStatus.this, response.getString(("message")), Toast.LENGTH_LONG).show();
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
                Toast.makeText(ActiveStatus.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        };
    }
}