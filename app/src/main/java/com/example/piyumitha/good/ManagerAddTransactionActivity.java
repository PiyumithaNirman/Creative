package com.example.piyumitha.good;

import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ManagerAddTransactionActivity extends AppCompatActivity {
    String projectId;
    Spinner spinnerType;
    TextInputEditText editTextDescription, editTextAmount;
    Button buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_add_transaction);

        spinnerType = findViewById(R.id.spinner_type);
        editTextDescription = findViewById(R.id.et_description);
        editTextAmount = findViewById(R.id.et_amount);
        buttonAdd = findViewById(R.id.btn_add);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTransaction();
            }
        });

        projectId = getIntent().getStringExtra("projectId");
        spinnerType.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"income", "expense"}));
    }

    void addTransaction() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + getSharedPreferences("my_preferences", MODE_PRIVATE).getString("access_token", ""));

        try {
            final DataRequest request = new DataRequest(
                    this,
                    Request.Method.POST,
                    Constants.MANAGER_ADD_TRANSACTION,
                    headers,
                    new JSONObject()
                            .put("projectID", projectId)
                            .put("type", spinnerType.getSelectedItem().toString())
                            .put("description", editTextDescription.getText().toString())
                            .put("amount", editTextAmount.getText().toString())
            );
            request.sendRequest(
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (request.statusCode == 200) {
                                try {
                                    new AlertDialog.Builder(ManagerAddTransactionActivity.this)
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
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    new AlertDialog.Builder(ManagerAddTransactionActivity.this)
                                            .setTitle("Error")
                                            .setMessage(response.getString("Failed to add transaction."))
                                            .setPositiveButton(
                                                    "Close",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    }
                                            ).show();
                                } catch (JSONException e) {
                                    new AlertDialog.Builder(ManagerAddTransactionActivity.this)
                                            .setTitle("Error")
                                            .setMessage(e.toString())
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
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            new AlertDialog.Builder(ManagerAddTransactionActivity.this)
                                    .setTitle("Error")
                                    .setMessage(error.toString())
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
            );
        } catch (JSONException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}