package com.example.piyumitha.good;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EmployeeRequestLeave extends AppCompatActivity {
    RadioGroup radioGroup1, radioGroup2;
    DatePicker datePicker;
    TextInputEditText editTextReason;
    Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_request_leave);

        radioGroup1 = findViewById(R.id.radio_group1);
        radioGroup2 = findViewById(R.id.radio_group2);

        datePicker = findViewById(R.id.date_picker);

        editTextReason = findViewById(R.id.et_reason);

        buttonSubmit = findViewById(R.id.buttonSubmit);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });

    }

    void sendRequest() {
        String token = getSharedPreferences("my_preferences", MODE_PRIVATE).getString("access_token", "");
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);

        try {
            final DataRequest request = new DataRequest(
                    this,
                    Request.Method.POST,
                    Constants.EMPLOYEE_REQUEST_LEAVE,
                    headers,
                    new JSONObject()
                            .put("typeOne", radioGroup1.getCheckedRadioButtonId() == R.id.rb1 ? "special" : "normal")
                            .put("typeTwo", radioGroup1.getCheckedRadioButtonId() == R.id.rb3 ? "full" : "half")
                            .put("leaveDate", new SimpleDateFormat("yyyy-MM-DD")
                                    .format(
                                            new Date(
                                                    datePicker.getYear(),
                                                    datePicker.getMonth(),
                                                    datePicker.getDayOfMonth()
                                            )
                                    )
                            )
                            .put("reason", editTextReason.getText().toString())
            );
            request.sendRequest(
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (request.statusCode == 200 || request.statusCode == 304) {
                                try {
                                    new AlertDialog.Builder(EmployeeRequestLeave.this)
                                            .setTitle("Success")
                                            .setMessage(response.getString("message"))
                                            .setPositiveButton(
                                                    "Close",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    }
                                            ).show();
                                    setResult(RESULT_OK);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                new AlertDialog.Builder(EmployeeRequestLeave.this)
                                        .setTitle("Error")
                                        .setMessage("Error occurred")
                                        .setPositiveButton(
                                                "Close",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                }
                                        ).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(EmployeeRequestLeave.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        } catch (JSONException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
