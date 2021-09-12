package com.anf.covid_smart;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class APIService {
    //Mervyn
    //final String url ="http://10.0.2.2:8080/FYP-Backend/api";
    final String url ="http://192.168.50.27:8080/FYP-Backend/api";

    IResponse mResponseCallback = null;
    Context mContext;

    APIService(IResponse resultCallback, Context context){
        mResponseCallback = resultCallback;
        mContext = context;
    }

    public void getMethod(String tag, String suffix){
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
            });

            queue.add(jsonObj);

        }catch(Exception e){

        }
    }

    public void postMethod(String tag, String suffix,JSONObject sendObj){
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
            });

            queue.add(jsonObj);

        }catch(Exception e){

        }
    }
}
