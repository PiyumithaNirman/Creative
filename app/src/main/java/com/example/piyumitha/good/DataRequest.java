package com.example.piyumitha.good;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Map;

public class DataRequest {
    private Context context;
    private int method;
    private String url;
    private Response.Listener<JSONObject> responseListener;
    private Response.ErrorListener errorListener;
    private Map<String, String> headers;
    private JSONObject body;

    public int statusCode;

    public DataRequest(Context context, int method, String url, Map<String, String> headers, JSONObject body) {
        this.context = context;
        this.method = method;
        this.url = url;
        this.headers = headers;
        this.body = body;
    }

    void sendRequest(final Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
                method,
                url,
                body,
                responseListener,
                errorListener

        ) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                statusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (headers != null) {
                    return headers;
                }
                return super.getHeaders();
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                if (volleyError.networkResponse != null)
                    statusCode = volleyError.networkResponse.statusCode;
                return super.parseNetworkError(volleyError);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }
        };
        Volley.newRequestQueue(context).add(jsonArrayRequest);
    }
}
