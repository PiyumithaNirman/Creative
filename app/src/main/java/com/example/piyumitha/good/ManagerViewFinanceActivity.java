package com.example.piyumitha.good;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ManagerViewFinanceActivity extends AppCompatActivity {
    ListView listView;

    Finance[] finances;

    String projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_show_all_projects);

        listView = findViewById(R.id.list_View);

        projectId = getIntent().getStringExtra("projectId");

        sendRequest();
    }

    void sendRequest() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constants.MANAGER_VIEW_FINANCE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseString) {
                        Log.d("MY_APP", responseString);
                        Toast.makeText(ManagerViewFinanceActivity.this, responseString, Toast.LENGTH_SHORT).show();

                        try {
                            JSONObject response = new JSONObject(responseString);
                            JSONArray arr = response.getJSONArray("result");
                            finances = new Finance[arr.length()];
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                Finance finance = new Finance(
                                        obj.getString("type"),
                                        obj.getString("description"),
                                        obj.getString("amount"),
                                        obj.getString("timeStamp")
                                );
                                finances[i] = finance;
                            }
                            updateList();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ManagerViewFinanceActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("projectID", projectId);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getSharedPreferences("my_preferences", MODE_PRIVATE).getString("access_token", ""));

                return headers;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    void updateList() {
        if (finances.length == 0) {
            Toast.makeText(this, "No Items", Toast.LENGTH_SHORT).show();
        }
        listView.setAdapter(
                new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return finances.length;
                    }

                    @Override
                    public Finance getItem(int position) {
                        return finances[position];
                    }

                    @Override
                    public long getItemId(int position) {
                        return position;
                    }

                    @Override
                    public View getView(final int position, View convertView, ViewGroup parent) {
                        if (convertView == null) {
                            convertView = getLayoutInflater().inflate(R.layout.finance_list_item, parent, false);

                            TextView name = convertView.findViewById(R.id.tv_type);
                            TextView type = convertView.findViewById(R.id.tv_description);
                            TextView budget = convertView.findViewById(R.id.tv_amount);
                            TextView description = convertView.findViewById(R.id.tv_timestamp);

                            name.setText("Leave Type : " + getItem(position).type);
                            type.setText("Description : " + getItem(position).description);
                            budget.setText("Amount : " + getItem(position).amount);
                            description.setText("Timestamp : " + getItem(position).timestamp);
                        }
                        return convertView;
                    }
                }
        );
    }

    class Finance {
        String type, description, amount, timestamp;

        public Finance(String type, String description, String amount, String timestamp) {
            this.type = type;
            this.description = description;
            this.amount = amount;
            this.timestamp = timestamp;
        }
    }
}
