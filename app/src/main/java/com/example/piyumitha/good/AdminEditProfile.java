package com.example.piyumitha.good;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdminEditProfile extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_profile);

        listView = findViewById(R.id.list_View);

        sendRequest();
    }

    void sendRequest() {
        String token = getSharedPreferences("my_preferences", MODE_PRIVATE).getString("access_token", "");
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        final DataRequest dataRequest;

        dataRequest = new DataRequest(
                AdminEditProfile.this,
                Request.Method.GET,
                Constants.ADMIN_DETAILS_URL,
                headers,
                null
        );

        dataRequest.sendRequest(
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (dataRequest.statusCode == 200 || dataRequest.statusCode == 304) {
                            Toast.makeText(AdminEditProfile.this, response.toString(), Toast.LENGTH_SHORT).show();
                            try {
                                JSONObject result = response.getJSONObject("result");

                                ListEntry[] listEntries = new ListEntry[4];
                                listEntries[0] = new ListEntry("Company name", result.getString("companyName"));
                                listEntries[1] = new ListEntry("Company Email", result.getString("companyEmail"));
                                listEntries[2] = new ListEntry("Company Address", result.getString("companyAddress"));
                                listEntries[3] = new ListEntry("Company Telephone", result.getString("companyTelephone"));

                                listView.setAdapter(new ListViewAdapter<ListEntry>(listEntries) {
                                    @Override
                                    public View getView(int position, View convertView, ViewGroup parent) {
                                        if (convertView == null) {
                                            convertView = getLayoutInflater().inflate(R.layout.list_view_item, parent, false);
                                            TextView title = convertView.findViewById(R.id.text_view_title);
                                            TextView content = convertView.findViewById(R.id.text_view_content);
                                            title.setText(this.getItem(position).title);
                                            content.setText(this.getItem(position).content);
                                        }
                                        return convertView;
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d("MY_APP", response.toString());
                        } else {
                            Toast.makeText(AdminEditProfile.this, String.valueOf(dataRequest.statusCode), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AdminEditProfile.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    class ListEntry {
        String title;
        String content;

        public ListEntry(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }
}