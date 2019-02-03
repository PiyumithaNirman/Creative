package com.example.piyumitha.good;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdminAddemployee extends AppCompatActivity {

    EditText EmployeeName, EmployeeUname, EmployeeEmail, EmployeeSpe;
    Spinner spinnerRole;
    Button AddEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_addemployee);

        EmployeeName = findViewById(R.id.editemName);
        EmployeeUname = findViewById(R.id.editemUe);
        spinnerRole = findViewById(R.id.spinner_role);
        EmployeeEmail = findViewById(R.id.editemEmail);
        EmployeeSpe = findViewById(R.id.editemSpe);
        AddEmployee = findViewById(R.id.buttonSubmitem);

        AddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckEditTextIsNotEmpty()) {
                    // If EditText is not empty and CheckEditText = True then this block will execute.
                    addEmployee();
                } else {
                    // If EditText is empty then this block will execute .
                    Toast.makeText(AdminAddemployee.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
                }

            }
        });
        roleList();
    }

    private void addEmployee() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + getSharedPreferences("my_preferences", MODE_PRIVATE).getString("access_token", ""));

        try {
            final DataRequest request = new DataRequest(
                    this,
                    Request.Method.POST,
                    Constants.ADMIN_ADD_EMPLOYEE_URL,
                    headers,
                    new JSONObject()
                            .put("empName", EmployeeName.getText().toString())
                            .put("userName", EmployeeUname.getText().toString())
                            .put("roleName", spinnerRole.getSelectedItem().toString())
                            .put("empEmail", EmployeeEmail.getText().toString())
                            .put("speciality", EmployeeSpe.getText().toString())

            );
            request.sendRequest(
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();
                            Toast.makeText(AdminAddemployee.this, response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(AdminAddemployee.this, error.toString(), Toast.LENGTH_SHORT).show();
                            if (request.statusCode == 400) {
                                new AlertDialog.Builder(AdminAddemployee.this)
                                        .setTitle("Error")
                                        .setMessage("Role already exist")
                                        .setNegativeButton("Close", null)
                                        .show();
                            } else if (request.statusCode == 401) {
                                new AlertDialog.Builder(AdminAddemployee.this)
                                        .setTitle("Error")
                                        .setMessage("Error sending email")
                                        .setNegativeButton("Close", null)
                                        .show();
                            } else {
                                showToast(error.toString());
                            }
                        }
                    }
            );
        } catch (JSONException e) {
            progressDialog.dismiss();
            showToast(e.getMessage());
        }
    }

    public boolean CheckEditTextIsNotEmpty() {
        String EmployeeNameHolder = EmployeeName.getText().toString();
        String EmployeeUnameHolder = EmployeeUname.getText().toString();
        String EmployeeRoleHolder = spinnerRole.getSelectedItem().toString();
        String EmployeeEmailHolder = EmployeeEmail.getText().toString();
        String EmployeeSpeHolder = EmployeeSpe.getText().toString();

        if (TextUtils.isEmpty(EmployeeNameHolder) ||
                TextUtils.isEmpty(EmployeeUnameHolder) ||
                TextUtils.isEmpty(EmployeeRoleHolder) ||
                TextUtils.isEmpty(EmployeeEmailHolder) ||
                TextUtils.isEmpty(EmployeeSpeHolder)) {
            return false;
        } else {
            return true;
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void roleList() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + getSharedPreferences("my_preferences", MODE_PRIVATE).getString("access_token", ""));

        final DataRequest request = new DataRequest(
                this,
                Request.Method.GET,
                Constants.ADMIN_ADD_EMPLOYEE_URL,
                headers,
                null
        );

                request.sendRequest(
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("MY_APP", String.format("%d%s", request.statusCode, response.toString()));
                        ArrayList<String> roleList = new ArrayList<>();
                        if (request.statusCode == 200 || request.statusCode == 304) {
                            try {
                                JSONArray arr = response.getJSONArray("list");
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject obj = arr.getJSONObject(i);
                                    roleList.add(obj.getString("roleName"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            spinnerRole.setAdapter(new ArrayAdapter<String>(AdminAddemployee.this, android.R.layout.simple_spinner_dropdown_item, roleList));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("MY_APP", error.toString());
                    }
                }
        );
    }
}