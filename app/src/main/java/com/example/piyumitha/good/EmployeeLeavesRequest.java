package com.example.piyumitha.good;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EmployeeLeavesRequest extends AppCompatActivity {
    TextView textViewLeaves;

    Button requestLeave, leaveHistory;

    int leaves = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_leaves_request);

        textViewLeaves = findViewById(R.id.textView8);
        requestLeave = findViewById(R.id.buttonReqstLeave);
        leaveHistory = findViewById(R.id.buttonLeaveHistory);

        requestLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leaves > 0) {
                    startActivityForResult(new Intent(EmployeeLeavesRequest.this, EmployeeRequestLeave.class), 0);
                } else {
                    new AlertDialog.Builder(EmployeeLeavesRequest.this)
                            .setTitle("Failed")
                            .setMessage("Your leave requests are over")
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
        });

        leaveHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmployeeLeavesRequest.this, EmployeeShowLeaveHistory.class));
            }
        });
        sendRequest();
    }

    void sendRequest() {
        String token = getSharedPreferences("my_preferences", MODE_PRIVATE).getString("access_token", "");
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);

        final DataRequest request = new DataRequest(
                this,
                Request.Method.GET,
                Constants.EMPLOYEE_LEAVES,
                headers,
                null
        );
        request.sendRequest(
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(EmployeeLeavesRequest.this, response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            leaves = Integer.parseInt(response.getString("remainingLeaves"));
                            textViewLeaves.setText(String.format("You have %d leaves remaining", leaves));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EmployeeLeavesRequest.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            sendRequest();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
