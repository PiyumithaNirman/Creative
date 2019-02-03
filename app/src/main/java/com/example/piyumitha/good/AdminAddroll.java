package com.example.piyumitha.good;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public class AdminAddroll extends AppCompatActivity {

    EditText RollName, Description, BasicSalary, OtRate;
    Button AddRoll, ShowRolls;
    String RollNameHolder, DescriptionHolder, BasicSalaryHolder, OtRateHolder;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_addroll);

        RollName = (EditText) findViewById(R.id.editName);
        Description = (EditText) findViewById(R.id.editDiscription);
        BasicSalary = (EditText) findViewById(R.id.editBasicSalary);
        OtRate = (EditText) findViewById(R.id.editOtRate);

        AddRoll = (Button) findViewById(R.id.buttonSubmit);
        //ShowRolls = (Button) findViewById(R.id.buttonShow);

        AddRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckEditTextIsNotEmpty()) {
                    // If EditText is not empty and CheckEditText = True then this block will execute.
                    AddRoll(RollNameHolder, DescriptionHolder, BasicSalaryHolder, OtRateHolder);

                } else {
                    // If EditText is empty then this block will execute .
                    Toast.makeText(AdminAddroll.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
                }

            }
        });

       /*ShowRolls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ShowAllRoleActivity.class);
                startActivity(intent);
            }
        });*/
    }

    public void AddRoll(final String S_RollName, final String S_Disc, final String S_BasicSal, final String S_OtRate) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + getSharedPreferences("my_preferences", MODE_PRIVATE).getString("access_token", ""));

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding role...");

        try {
            final DataRequest dataRequest = new DataRequest(
                    AdminAddroll.this,
                    Request.Method.POST,
                    Constants.ADMIN_ADD_ROLE_URL,
                    headers,
                    new JSONObject()
                            .put("roleName", S_RollName)
                            .put("description", S_Disc)
                            .put("basicSalary", Float.parseFloat(S_BasicSal))
                            .put("OTRate", Float.parseFloat(S_OtRate))

            );
            dataRequest.sendRequest(
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();

                            if (dataRequest.statusCode == 200 || dataRequest.statusCode == 304) {
                                try {
                                    new AlertDialog.Builder(AdminAddroll.this)
                                            .setTitle("Success")
                                            .setMessage(response.getString("message"))
                                            .setNegativeButton("Close", null)
                                            .show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                showToast(response.toString());
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            if (dataRequest.statusCode == 422) {
                                new AlertDialog.Builder(AdminAddroll.this)
                                        .setTitle("Error")
                                        .setMessage("Role already exist")
                                        .setNegativeButton("Close", null)
                                        .show();
                            } else if (dataRequest.statusCode == 404) {
                                new AlertDialog.Builder(AdminAddroll.this)
                                        .setTitle("Error")
                                        .setMessage("Error adding employee")
                                        .setNegativeButton("Close", null)
                                        .show();
                            } else {
                                showToast(error.toString());
                            }
                        }
                    });
        } catch (JSONException e) {
            progressDialog.dismiss();
            showToast(e.getMessage());
        }
    }


    public boolean CheckEditTextIsNotEmpty() {
        RollNameHolder = RollName.getText().toString();
        DescriptionHolder = Description.getText().toString();
        BasicSalaryHolder = BasicSalary.getText().toString();
        OtRateHolder = OtRate.getText().toString();
        if (TextUtils.isEmpty(RollNameHolder) || TextUtils.isEmpty(DescriptionHolder) || TextUtils.isEmpty(BasicSalaryHolder) || TextUtils.isEmpty(BasicSalaryHolder)) {
            return false;
        } else {
            return true;
        }

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}