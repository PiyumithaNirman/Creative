package com.example.piyumitha.good;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class DisplayEmployee extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_employee);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // if(savedInstanceState == null) {
        // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
        // new Account()).commit();
        //  navigationView.setCheckedItem(R.id.nav_account);
        //}
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_account:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Account()).commit();
                break;
            case R.id.nav_company:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Company()).commit();
                break;
            case R.id.nav_search:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Search()).commit();
                break;
            case R.id.nav_setting:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Setting()).commit();
                break;
            case R.id.nav_feedback:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FeedBack()).commit();
                break;
            case R.id.nav_ratethisapp:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new RateThisApp()).commit();
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{

            super.onBackPressed();


        }
    }
    public void onButtonClick(View v)
    {
        if(v.getId ()== R.id.Bprojectview)
        {
            Intent i = new  Intent(DisplayEmployee.this, EmployeeProjectView.class);
            startActivity(i);

        }

        if(v.getId ()== R.id.Bleavesemp)
        {
            Intent i = new  Intent(DisplayEmployee.this, EmployeeLeavesRequest.class);
            startActivity(i);

        }

        if(v.getId ()== R.id.Bprofileemp)
        {
            Intent i = new  Intent(DisplayEmployee.this, EmployeeProfileView.class);
            startActivity(i);

        }

        if(v.getId ()== R.id.Bsalaryhis)
        {
            Intent i = new  Intent(DisplayEmployee.this, EmployeeSalaryHistory.class);
            startActivity(i);

        }

        if(v.getId ()== R.id.Bleavereqests)
        {
            Intent i = new  Intent(DisplayEmployee.this, EmployeeLeavesRequest.class);
            startActivity(i);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menuLogout:
                SharedPrefManager.getInstance(this).logout();
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.menuSettings:
                Toast.makeText(this, "You clicked settings", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }
}
