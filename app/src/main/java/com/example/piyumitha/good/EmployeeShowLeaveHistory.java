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

public class EmployeeShowLeaveHistory extends AppCompatActivity {
    ListView listView;

    Leave[] leaves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_show_all_projects);

        listView = findViewById(R.id.list_View);

        sendRequest();
    }

    void sendRequest() {
        StringRequest request = new StringRequest(
                Request.Method.GET,
                Constants.EMPLOYEE__LEAVE_HISTORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseString) {
                        Log.d("MY_APP", responseString);
                        Toast.makeText(EmployeeShowLeaveHistory.this, responseString, Toast.LENGTH_SHORT).show();

                        try {
                            JSONObject response = new JSONObject(responseString);
                            JSONArray arr = response.getJSONArray("result");
                            leaves = new Leave[arr.length()];
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                Leave leave = new Leave(
                                        obj.getString("timeStamp"),
                                        obj.getString("typeOne"),
                                        obj.getString("typeTwo"),
                                        obj.getString("reason")
                                );
                                leaves[i] = leave;
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
                        Toast.makeText(EmployeeShowLeaveHistory.this, error.toString(), Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }
        ) {
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
        if (leaves.length == 0) {
            Toast.makeText(this, "No Items", Toast.LENGTH_SHORT).show();
        }
        listView.setAdapter(
                new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return leaves.length;
                    }

                    @Override
                    public Leave getItem(int position) {
                        return leaves[position];
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

                            name.setText("Timestamp  : " + getItem(position).timeStamp);
                            type.setText("Specialty  : " + getItem(position).typeOne);
                            budget.setText("Full / Half: " + getItem(position).typeTwo);
                            description.setText("Reason     : " + getItem(position).reason);
                        }
                        return convertView;
                    }
                }
        );
    }

    class Leave {
        String timeStamp, typeOne, typeTwo, reason;

        public Leave(String timeStamp, String typeOne, String typeTwo, String reason) {
            this.timeStamp = timeStamp;
            this.typeOne = typeOne;
            this.typeTwo = typeTwo;
            this.reason = reason;
        }
    }
}
