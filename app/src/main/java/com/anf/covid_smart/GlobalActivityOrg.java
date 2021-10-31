package com.anf.covid_smart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

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

public class GlobalActivityOrg extends AppCompatActivity {

    TextView totalCasesOrg, totalDeathsOrg;
    ListView listview;
    ArrayList<Country> countryList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_org);

        totalCasesOrg = findViewById(R.id.totalCasesOrg);
        totalDeathsOrg = findViewById(R.id.totalDeathsOrg);
        listview = findViewById(R.id.countries_list_org);

        BottomNavigationView btmNavView = findViewById(R.id.bottom_navigation);
        btmNavView.setSelectedItemId(R.id.nav_home);

        btmNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){

                    case R.id.nav_home:
                        Intent homeintent = new Intent(getApplicationContext(), OrgHomeActivity.class);
                        homeintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeintent);
                        return true;
                    case R.id.nav_alert:
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

        getTotalCases();
        //getGlobalCases();
    }
    private void getTotalCases() {
        Log.i("inside","inside");
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObj = new JsonObjectRequest("https://api.covid19api.com/summary", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String resActiveCases = response.getJSONObject("Global").getString("TotalConfirmed");
                    String resClosedCases = response.getJSONObject("Global").getString("TotalDeaths");
                    JSONArray countries = response.getJSONArray("Countries");
                    for (int i = 0; i < countries.length(); i++)
                    {
                        JSONObject country = countries.getJSONObject(i);
                        String C = country.getString("Country");
                        String TC = country.getString("TotalConfirmed");
                        String NC = country.getString("NewConfirmed");
                        countryList.add(new Country(C,TC,NC));
                    }
                    totalCasesOrg.setText(resActiveCases);
                    totalDeathsOrg.setText(resClosedCases);
                    CountryAdapter countryAdapter = new CountryAdapter(GlobalActivityOrg.this, R.layout.countries_case, countryList);
                    listview.setAdapter(countryAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", error.toString());

            }
        });
        queue.add(jsonObj);
    }
    private void getGlobalCases(){

        ArrayList<Country> countryList = new ArrayList<>();

        Log.i("inside","inside");
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObj = new JsonObjectRequest("https://api.covid19api.com/summary", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray countries = response.getJSONArray("Countries");
                    for (int i = 0; i < countries.length(); i++)
                    {
                        JSONObject country = countries.getJSONObject(i);
                        String C = country.getString("Country");
                        String TC = country.getString("TotalConfirmed");
                        String NC = country.getString("NewConfirmed");
                        countryList.add(new Country(C,TC,NC));
                    }

                    CountryAdapter countryAdapter = new CountryAdapter(GlobalActivityOrg.this, R.layout.countries_case, countryList);
                    listview.setAdapter(countryAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

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