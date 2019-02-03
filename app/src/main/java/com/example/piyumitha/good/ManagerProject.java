package com.example.piyumitha.good;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ManagerProject extends AppCompatActivity {

    EditText ProjectName, ProjectBudget, ProjectDesc;
    Spinner ProjectType;
    Button AddProjects, ShowProjects;
    String ProjectNameHolder, ProjectTypeHolder, ProjectBudgetHolder, ProjectDescHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_project);

        ProjectName = (EditText) findViewById(R.id.editProjenam);
        ProjectType = findViewById(R.id.spinnerType);
        ProjectBudget = (EditText) findViewById(R.id.ediProBug);
        ProjectDesc = (EditText) findViewById(R.id.ediProdis);


        AddProjects = (Button) findViewById(R.id.buttonSubmitPro);
        ShowProjects = (Button) findViewById(R.id.buttonProjectShow);

        AddProjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckEditTextIsNotEmpty()) {
                    addProject();

                } else {
                    Toast.makeText(ManagerProject.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
                }

            }
        });

        ProjectType.setAdapter(
                new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        new String[]{"client", "company"}
                ));

        ShowProjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManagerProject.this, ManagerShowAllProjectsActivity.class));
            }
        });
    }

    private void addProject() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding project...");
        progressDialog.show();

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + getSharedPreferences("my_preferences", MODE_PRIVATE).getString("access_token", ""));

        try {
            final DataRequest request = new DataRequest(
                    this,
                    Request.Method.POST,
                    Constants.MANAGER_ADD_PROJECT,
                    headers,
                    new JSONObject()
                            .put("projectName", ProjectName.getText().toString())
                            .put("type", ProjectType.getSelectedItem().toString())
                            .put("budget", ProjectBudget.getText().toString())
                            .put("projectDescription", ProjectDesc.getText().toString())
            );
            request.sendRequest(
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();
                            if (request.statusCode == 200 || request.statusCode == 304) {
                                new AlertDialog.Builder(ManagerProject.this)
                                        .setTitle("Success")
                                        .setMessage("Successfully created project.")
                                        .setNegativeButton(
                                                "Retry",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                        addProject();
                                                    }
                                                }
                                        )
                                        .show();
                            } else {
                                new AlertDialog.Builder(ManagerProject.this)
                                        .setTitle("Error")
                                        .setMessage("Error creating project.")
                                        .setNegativeButton(
                                                "Retry",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                        addProject();
                                                    }
                                                }
                                        )
                                        .show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            new AlertDialog.Builder(ManagerProject.this)
                                    .setTitle("Error")
                                    .setMessage("Error creating project.\n" + error.toString())
                                    .setNegativeButton(
                                            "Retry",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    addProject();
                                                }
                                            }
                                    )
                                    .show();
                        }
                    }
            );

        } catch (JSONException e) {
            new AlertDialog.Builder(ManagerProject.this)
                    .setTitle("Error")
                    .setMessage("Error creating project.\n" + e.toString())
                    .setNegativeButton(
                            "Retry",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    addProject();
                                }
                            }
                    )
                    .show();
        }
    }

    public boolean CheckEditTextIsNotEmpty() {
        ProjectNameHolder = ProjectName.getText().toString();
        ProjectTypeHolder = ProjectType.getSelectedItem().toString();
        ProjectBudgetHolder = ProjectBudget.getText().toString();
        ProjectDescHolder = ProjectDesc.getText().toString();

        if (TextUtils.isEmpty(ProjectNameHolder) || TextUtils.isEmpty(ProjectTypeHolder) || TextUtils.isEmpty(ProjectBudgetHolder) || TextUtils.isEmpty(ProjectDescHolder)) {
            return false;
        } else {
            return true;
        }

    }
}

