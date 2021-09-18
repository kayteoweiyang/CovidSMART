package com.anf.covid_smart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class GlobalActivity extends AppCompatActivity {
    TextView totalCasesTxt, totalDeathsTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global);


        totalCasesTxt = findViewById(R.id.totalCasesTxt);
        totalDeathsTxt = findViewById(R.id.totalDeathsTxt);


        BottomNavigationView btmNavView = findViewById(R.id.bottom_navigation);
        btmNavView.setSelectedItemId(R.id.nav_home);

        btmNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){

                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.nav_alert:
                        return true;
                    case R.id.nav_profile:
                        return true;
                    case R.id.nav_logout:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        getGlobalCases();
    }
    private void getGlobalCases() {
        Log.i("inside","inside");
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObj = new JsonObjectRequest("https://api.covid19api.com/summary", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String resActiveCases = response.getJSONObject("Global").getString("TotalConfirmed");
                    String resClosedCases = response.getJSONObject("Global").getString("TotalDeaths");
                    totalCasesTxt.setText(resActiveCases);
                    totalDeathsTxt.setText(resClosedCases);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("response", response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", error.toString());

            }
        });
        queue.add(jsonObj);
    }

}