package com.anf.covid_smart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AlertActivity extends AppCompatActivity {
    ListView sg_rss;
    ArrayList<String> titles;
    ArrayList<String> links;
    TabHost tabhost;
    AsyncTask mtask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        sg_rss = findViewById(R.id.tab1_list);

        titles = new ArrayList<String>();
        links = new ArrayList<String>();

        tabhost = findViewById(R.id.tabhostAlert);
        tabhost.setup();

        TabHost.TabSpec tab1 = tabhost.newTabSpec("Singapore");
        tab1.setContent(R.id.tab_alert1);
        tab1.setIndicator("Singapore");
        tabhost.addTab(tab1);

        TabHost.TabSpec tab2 = tabhost.newTabSpec("International");
        tab2.setContent(R.id.tab_alert2);
        tab2.setIndicator("International");
        tabhost.addTab(tab2);


        BottomNavigationView btmNavView = findViewById(R.id.bottom_navigation);
        btmNavView.setSelectedItemId(R.id.nav_alert);

        btmNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){

                    case R.id.nav_home:
                        Intent homeintent = new Intent(getApplicationContext(), HomeActivity.class);
                        homeintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeintent);
                        mtask.cancel(true);
                        finish();
                        return true;
                    case R.id.nav_alert:
                        return true;
                    case R.id.nav_profile:
                        Intent profile = new Intent(getApplicationContext(), ProfileActivity.class);
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
        sg_rss.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri = Uri.parse(links.get(position));
                Intent uriIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(uriIntent);
            }
        });

        mtask = new ProcessinBackground().execute();

    }
    public InputStream getInputStream (URL url)
    {
        try{
            return url.openConnection().getInputStream();
        }
        catch (IOException e){
            return null;
        }
    }
    public class ProcessinBackground extends AsyncTask<Integer, Void, Exception> {
        ProgressDialog progressDialog = new ProgressDialog(AlertActivity.this);
        Exception exception = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("RSS", "Loading Rss...");
        }

        @Override
        protected Exception doInBackground(Integer... integers) {
            Log.i("RSS", "In Background...");
            try {
                URL url = new URL("https://www.travel-advisory.info/rss");

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(getInputStream(url), "UTF_8");

                boolean insideItem = false;
                int eventType = xpp.getEventType();
                Log.i("RSS", String.valueOf(eventType));
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    Log.i("Item", String.valueOf(xpp.getName()));
                    if (eventType == XmlPullParser.START_TAG) {
                        Log.i("StartTag", String.valueOf(xpp.getName()));
                        if (xpp.getName().equalsIgnoreCase("item")) {
                            insideItem = true;
                        } else if (xpp.getName().equalsIgnoreCase("title")) {
                            if (insideItem == true) {
                                titles.add(xpp.nextText());
                            }
                        } else if (xpp.getName().equalsIgnoreCase("link")) {
                            if (insideItem == true) {
                                links.add(xpp.nextText());
                            }
                        }
                    } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                        insideItem = false;
                    }
                    xpp.next();
                }
            } catch (MalformedURLException e) {
                exception = e;
                Log.i("RSS", String.valueOf(exception));
            } catch (IOException e) {
                exception = e;
                Log.i("RSS", String.valueOf(exception));
            } catch (XmlPullParserException e) {
                exception = e;
                Log.i("RSS", String.valueOf(exception));
            }

            return exception;
        }

        @Override
        protected void onPostExecute(Exception s) {
            super.onPostExecute(s);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(AlertActivity.this, android.R.layout.simple_list_item_1, titles);
            sg_rss.setAdapter(adapter);

            Log.i("RSS", String.valueOf(titles.size()));

            Log.i("RSS", "Done");
        }
    }

}