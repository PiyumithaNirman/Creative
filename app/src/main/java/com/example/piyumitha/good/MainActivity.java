package com.example.piyumitha.good;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;


import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout relativeLayout1, relativeLayout2;
    private Button button1, button2;
    private EditText editText1, editText2;

    Handler handler = new Handler(Looper.getMainLooper());

    /*Handler handler = new Handler();
    Runnable runnable = new Runnable()
    {
        @Override
        public void run()
        {
            relativeLayout1.setVisibility(View.VISIBLE);
            relativeLayout2.setVisibility(View.VISIBLE);
        }
    };*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*relativeLayout1 = (RelativeLayout) findViewById(R.id.relay1);
        relativeLayout2 = (RelativeLayout) findViewById(R.id.relay2);
        handler.postDelayed(runnable, 2000);*/

        button1 = (Button) findViewById(R.id.btn_login);
        button2 = (Button) findViewById(R.id.btn_signup);
        editText1 = (EditText) findViewById(R.id.ed_uname);
        editText2 = (EditText) findViewById(R.id.ed_pass);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
    }


    private void loginUser() {

        final String username = editText1.getText().toString().trim();
        final String password = editText2.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please Wait...");

        progressDialog.show();

        try {
            final DataRequest dataRequest = new DataRequest(
                    MainActivity.this,
                    Request.Method.POST,
                    Constants.LOGIN_URL,
                    null,
                    new JSONObject()
                            .put("email", username)
                            .put("password", password)
            );
            dataRequest.sendRequest(
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();
                            try {
                                if (dataRequest.statusCode == 200 || dataRequest.statusCode == 304 || dataRequest.statusCode == 0) {
                                    // save token
                                    SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
                                    preferences.edit().putString("access_token", response.getString("token")).apply();

                                    String userType = response.getString("userType");

                                    Toast.makeText(MainActivity.this, userType, Toast.LENGTH_SHORT).show();

                                    if (userType.equals("1")) {
                                        startActivity(new Intent(getApplicationContext(), DisplayAdmin.class));

                                    } else if (userType.equals("2")) {
                                        startActivity(new Intent(getApplicationContext(), DisplayManager.class));

                                    } else if (userType.equals("4")) {
                                        startActivity(new Intent(getApplicationContext(), DisplayProjectLeader.class));

                                    } else if (userType.equals("3")) {
                                        startActivity(new Intent(getApplicationContext(), DisplayEmployee.class));

                                    }
                                    finish();
                                } else {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            response.getString("message"),
                                            Toast.LENGTH_LONG
                                    ).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();

                            Toast.makeText(
                                    getApplicationContext(),
                                    error.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });
        } catch (JSONException e) {
            progressDialog.dismiss();

            Toast.makeText(
                    getApplicationContext(),
                    e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == button1) {
            loginUser();

        }
        if (v == button2) {
            startActivity(new Intent(this, SignUp.class));
        }

    }
}
