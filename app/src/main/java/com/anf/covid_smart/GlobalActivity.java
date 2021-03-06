package com.anf.covid_smart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import org.w3c.dom.Text;

import java.util.ArrayList;

public class GlobalActivity extends AppCompatActivity {
    TextView totalCasesTxt, totalDeathsTxt;
    ListView listview;
    ArrayList<Country> countryList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global);

        totalCasesTxt = findViewById(R.id.totalCasesTxt);
        totalDeathsTxt = findViewById(R.id.totalDeathsTxt);
        listview = findViewById(R.id.countries_list);



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

        getTotalCases();
        //getGlobalCases();
    }
    private void getTotalCases() {
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
                    totalCasesTxt.setText(resActiveCases);
                    totalDeathsTxt.setText(resClosedCases);
                    CountryAdapter countryAdapter = new CountryAdapter(GlobalActivity.this, R.layout.countries_case, countryList);
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

                    CountryAdapter countryAdapter = new CountryAdapter(GlobalActivity.this, R.layout.countries_case, countryList);
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