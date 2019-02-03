package com.example.piyumitha.good;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ManagerShowAllProjectsActivity extends AppCompatActivity {
    ListView listView;

    Project[] projects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_show_all_projects);

        listView = findViewById(R.id.list_View);

        sendRequest();
    }

    void sendRequest() {

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + getSharedPreferences("my_preferences", MODE_PRIVATE).getString("access_token", ""));

        DataRequest request = new DataRequest(
                this,
                Request.Method.GET,
                Constants.MANAGER_ALL_PROJECT,
                headers, null
        );
        request.sendRequest(
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("MY_APP", response.toString());
                        //Toast.makeText(ManagerShowAllProjectsActivity.this, response.toString(), Toast.LENGTH_SHORT).show();

                        try {
                            JSONArray arr = response.getJSONArray("result");
                            projects = new Project[arr.length()];
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                Project project = new Project(
                                        obj.getString("projectId"),
                                        obj.getString("projectName"),
                                        obj.getString("type"),
                                        obj.getString("budget"),
                                        obj.getString("projectDescription"),
                                        obj.getString("status")
                                );
                                projects[i] = project;
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
                        Toast.makeText(ManagerShowAllProjectsActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    void updateList() {

        listView.setAdapter(
                new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return projects.length;
                    }

                    @Override
                    public Project getItem(int position) {
                        return projects[position];
                    }

                    @Override
                    public long getItemId(int position) {
                        return position;
                    }

                    @Override
                    public View getView(final int position, View convertView, ViewGroup parent) {
                        if (convertView == null) {
                            convertView = getLayoutInflater().inflate(R.layout.list_item, parent, false);
                            TextView name = convertView.findViewById(R.id.tv_project_name);
                            TextView type = convertView.findViewById(R.id.tv_project_type);
                            TextView budget = convertView.findViewById(R.id.tv_project_budget);
                            TextView description = convertView.findViewById(R.id.tv_project_description);
                            TextView status = convertView.findViewById(R.id.tv_project_status);
                            Button buttonAdd = convertView.findViewById(R.id.btn_add_transaction);
                            Button buttonView = convertView.findViewById(R.id.btn_view_transaction);

                            name.setText("Project Name : " + getItem(position).name);
                            type.setText("Project Type : " + getItem(position).type);
                            budget.setText("Project Budget : " + getItem(position).budget);
                            description.setText("Project Description : " + getItem(position).description);
                            status.setText("Project Status : " + getItem(position).status);

                            buttonAdd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ManagerShowAllProjectsActivity.this, ManagerAddTransactionActivity.class);
                                    intent.putExtra("projectId", getItem(position).projectId);
                                    startActivity(intent);
                                }
                            });

                            buttonView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ManagerShowAllProjectsActivity.this, ManagerViewFinanceActivity.class);
                                    intent.putExtra("projectId", getItem(position).projectId);
                                    startActivity(intent);}
                            });
                        }
                        return convertView;
                    }
                }
        );
    }

    class Project {
        String projectId, name, type, budget, description, status;

        public Project(String projectId, String name, String type, String budget, String description, String status) {
            this.projectId = projectId;
            this.name = name;
            this.type = type;
            this.budget = budget;
            this.description = description;
            this.status = status;
        }
    }
}
