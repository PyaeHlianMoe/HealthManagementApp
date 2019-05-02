package com.se_lab.se_proj;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;

import com.se_lab.se_proj.HealthMgmt.ActivityHealthMgmt;
import com.se_lab.se_proj.Nearby.FragmentMaps;
import com.se_lab.se_proj.Profile.FragmentSettings;
import com.google.firebase.auth.FirebaseAuth;
import android.widget.Toast;
import android.content.Intent;

public class ActivityMain extends AppCompatActivity implements MenuItem.OnMenuItemClickListener{

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    private Menu drawerMenu;
    private NavigationView navigation_view;

    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigation_view = (NavigationView) findViewById(R.id.nav_drawer_menu_view);

        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open,
                R.string.drawer_close);

        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        drawerMenu = navigation_view.getMenu();

        loadFragment(new FragmentDashboard());
        for(int i = 0; i < drawerMenu.size(); i++) {
            drawerMenu.getItem(i).setOnMenuItemClickListener(this);
        }


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_dashboard:
                drawerLayout.closeDrawers();
                loadFragment(new FragmentDashboard());
                // handle it
                break;
            case R.id.menu_healthmgmt:
                drawerLayout.closeDrawers();
                Intent intenthealth = new Intent(ActivityMain.this, ActivityHealthMgmt.class);
                startActivity(intenthealth);
                // handle it
                break;
            case R.id.menu_help:
                drawerLayout.closeDrawers();
                loadFragment(new FragmentHelp());
                // handle it
                break;
            case R.id.menu_profile:
                drawerLayout.closeDrawers();
                loadFragment(new FragmentSettings());
                // handle it
                break;
            case R.id.menu_nearby:
                drawerLayout.closeDrawers();
                loadFragment(new FragmentMaps());
                // do whatever
                break;
            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ActivityMain.this, ActivityLoginPage.class);
                startActivity(intent);
                finish();
                break;
            // and so on...
        }
        return false;
    }


    public void loadFragment(Fragment fragment) {
        // create a FragmentManager
        FragmentManager fm = getSupportFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.mainFrameLayout, fragment);
        fragmentTransaction.commit(); // save the changes
    }

}