package com.anf.covid_smart;

import com.android.volley.VolleyError;
import org.json.JSONObject;

public interface IResponse {
    void notifySuccess(String tag, JSONObject response);
    void notifyError(String tag, VolleyError error);
}
