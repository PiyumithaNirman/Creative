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

public class EmployeeSalaryHistory extends AppCompatActivity {
    ListView listView;

    SalaryHistory[] salaryHistories;

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
                Constants.EMPLOYEE__SALARY_HISTORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseString) {
                        try {
                            JSONObject response = new JSONObject(responseString);
                            JSONArray arr = response.getJSONArray("result");
                            salaryHistories = new SalaryHistory[arr.length()];
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                SalaryHistory salaryHistory = new SalaryHistory(
                                        obj.getString("salary"),
                                        obj.getString("allowance"),
                                        obj.getString("OTRate"),
                                        obj.getString("OTHours"),
                                        obj.getString("basicSalary")
                                );
                                salaryHistories[i] = salaryHistory;
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
                        Toast.makeText(EmployeeSalaryHistory.this, error.toString(), Toast.LENGTH_SHORT).show();
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
        if (salaryHistories.length == 0) {
            Toast.makeText(this, "No Items", Toast.LENGTH_SHORT).show();
        }
        listView.setAdapter(
                new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return salaryHistories.length;
                    }

                    @Override
                    public SalaryHistory getItem(int position) {
                        return salaryHistories[position];
                    }

                    @Override
                    public long getItemId(int position) {
                        return position;
                    }

                    @Override
                    public View getView(final int position, View convertView, ViewGroup parent) {
                        if (convertView == null) {
                            convertView = getLayoutInflater().inflate(R.layout.list_item2, parent, false);

                            TextView tv1 = convertView.findViewById(R.id.tv_1);
                            TextView tv2 = convertView.findViewById(R.id.tv_2);
                            TextView tv3 = convertView.findViewById(R.id.tv_3);
                            TextView tv4 = convertView.findViewById(R.id.tv_4);
                            TextView tv5 = convertView.findViewById(R.id.tv_5);

                            tv1.setText("Salary : " + getItem(position).salary);
                            tv2.setText("Allowance : " + getItem(position).allowance);
                            tv3.setText("OTRate : " + getItem(position).OTRate);
                            tv4.setText("OTHours : " + getItem(position).OTHours);
                            tv5.setText("Basic Salary : " + getItem(position).basicSalary);
                        }
                        return convertView;
                    }
                }
        );
    }

    class SalaryHistory {
        String salary;
        String allowance;
        String OTRate;
        String OTHours;
        String basicSalary;


        public SalaryHistory(String salary, String allowance, String OTRate, String OTHours, String basicSalary) {
            this.salary = salary;
            this.allowance = allowance;
            this.OTRate = OTRate;
            this.OTHours = OTHours;
            this.basicSalary = basicSalary;
        }
    }
}
