package com.anf.covid_smart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class AlertActivityOrg extends AppCompatActivity {

    ListView sg_rss, int_rss;
    ArrayList<String> titles, titles_i;
    ArrayList<String> links, links_i;
    TabHost tabhost;
    AsyncTask mtask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_org);

        sg_rss = findViewById(R.id.tab1_list_org);
        int_rss = findViewById(R.id.tab2_list_org);

        titles = new ArrayList<String>();
        titles_i = new ArrayList<String>();
        links = new ArrayList<String>();
        links_i  = new ArrayList<String>();

        tabhost = findViewById(R.id.tabhostAlertOrg);
        tabhost.setup();

        TabHost.TabSpec tab1 = tabhost.newTabSpec("Singapore");
        tab1.setContent(R.id.tab_alert1_org);
        tab1.setIndicator("Singapore");
        tabhost.addTab(tab1);

        TabHost.TabSpec tab2 = tabhost.newTabSpec("International");
        tab2.setContent(R.id.tab_alert2_org);
        tab2.setIndicator("International");
        tabhost.addTab(tab2);

        BottomNavigationView btmNavView = findViewById(R.id.bottom_navigation);
        btmNavView.setSelectedItemId(R.id.nav_alert);

        btmNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){

                    case R.id.nav_home:
                        Intent homeintent = new Intent(getApplicationContext(), OrgHomeActivity.class);
                        homeintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeintent);
                        if(!mtask.isCancelled()) {
                            Log.i("cancellation", "true");
                            mtask.cancel(true);
                        }
                        finish();
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
        int_rss.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri = Uri.parse(links_i.get(position));
                Intent uriIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(uriIntent);
            }
        });

        mtask = new ProcessinBackgroundOrg().execute();
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

    public class ProcessinBackgroundOrg extends AsyncTask<Integer, Void, Exception> {
        Exception exception = null;
        //by singapore
        @Override
        protected Exception doInBackground(Integer... integers) {
            Log.i("RSS", "In Background...");
            try {
                URL url = new URL("https://www.mfa.gov.sg/rss/travel-advisories");

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(getInputStream(url), "UTF_8");

                boolean insideItem = false;
                int eventType = xpp.getEventType();

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
                    eventType = xpp.nextToken();
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
            //for international
            try {
                URL url_i = new URL("https://www.travel-advisory.info/rss");

                XmlPullParserFactory i_factory = XmlPullParserFactory.newInstance();
                i_factory.setNamespaceAware(true);
                XmlPullParser xpp1 = i_factory.newPullParser();
                xpp1.setInput(getInputStream(url_i), "UTF_8");

                boolean insideItem = false;
                int eventType = xpp1.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    Log.i("Item", String.valueOf(xpp1.getName()));
                    if (eventType == XmlPullParser.START_TAG) {
                        Log.i("StartTag", String.valueOf(xpp1.getName()));
                        if (xpp1.getName().equalsIgnoreCase("item")) {
                            insideItem = true;
                        } else if (xpp1.getName().equalsIgnoreCase("title")) {
                            if (insideItem == true) {
                                titles_i.add(xpp1.nextText());
                            }
                        } else if (xpp1.getName().equalsIgnoreCase("link")) {
                            if (insideItem == true) {
                                links_i.add(xpp1.nextText());
                            }
                        }
                    } else if (eventType == XmlPullParser.END_TAG && xpp1.getName().equalsIgnoreCase("item")) {
                        insideItem = false;
                    }
                    eventType = xpp1.nextToken();
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

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(AlertActivityOrg.this, android.R.layout.simple_list_item_1, titles);
            sg_rss.setAdapter(adapter);
            ArrayAdapter<String> adapter_i = new ArrayAdapter<String>(AlertActivityOrg.this, android.R.layout.simple_list_item_1, titles_i);
            int_rss.setAdapter(adapter_i);
            Log.i("RSS", "Done");
        }
    }
}