package com.example.piyumitha.good;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Welcome extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        Thread xyz = new Thread() {
            public void run() {
                try {
                    sleep(2500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    Intent intent = new Intent(Welcome.this,MainActivity.class);
                    startActivity(intent);
                }

            }
        };
        xyz.start();
    }
    @Override
    protected void onPause()
    {
       super.onPause();
       finish();
    }
}
