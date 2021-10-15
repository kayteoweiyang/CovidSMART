package com.anf.covid_smart;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class APIService {
    final String url = "https://covid-smartapp-backend.herokuapp.com/api";
    MainActivity mainActivity = new MainActivity();
    String token = mainActivity.authToken;

    IResponse mResponseCallback = null;
    Context mContext;

    APIService(IResponse resultCallback, Context context){
        mResponseCallback = resultCallback;
        mContext = context;
    }

    public void getMethod(String tag, String suffix){
        String authToken = this.token;
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            JsonObjectRequest jsonObj = new JsonObjectRequest(this.url.concat(suffix), null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if(mResponseCallback != null)
                        mResponseCallback.notifySuccess(tag, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(mResponseCallback != null)
                        mResponseCallback.notifyError(tag, error);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Bearer ".concat(authToken) );
                    return params;
                }
            };

            queue.add(jsonObj);

        }catch(Exception e){

        }
    }

    public void getMethod(String tag, String suffix, JSONObject sendObj){
        String authToken = this.token;
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            JsonObjectRequest jsonObj = new JsonObjectRequest(this.url.concat(suffix), sendObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if(mResponseCallback != null)
                        mResponseCallback.notifySuccess(tag, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(mResponseCallback != null)
                        mResponseCallback.notifyError(tag, error);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Bearer ".concat(authToken) );
                    return params;
                }
            };

            queue.add(jsonObj);

        }catch(Exception e){

        }
    }

    public void postMethod(String tag, String suffix,JSONObject sendObj){
        String authToken = this.token;
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.POST ,this.url.concat(suffix),sendObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if(mResponseCallback != null)
                        mResponseCallback.notifySuccess(tag, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(mResponseCallback != null)
                        mResponseCallback.notifyError(tag, error);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Bearer ".concat(authToken) );
                    return params;
                }
            };



            queue.add(jsonObj);

        }catch(Exception e){

        }
    }

    public void putMethod(String tag, String suffix,JSONObject sendObj){
        String authToken = this.token;
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.POST ,this.url.concat(suffix),sendObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if(mResponseCallback != null)
                        mResponseCallback.notifySuccess(tag, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(mResponseCallback != null)
                        mResponseCallback.notifyError(tag, error);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Bearer ".concat(authToken) );
                    return params;
                }
            };



            queue.add(jsonObj);

        }catch(Exception e){

        }
    }
}
