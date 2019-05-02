package com.se_lab.se_proj.BaseNavigation;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.se_lab.se_proj.ActivityHelp;
import com.se_lab.se_proj.ActivityLoginPage;
import com.se_lab.se_proj.ActivityMain;
import com.se_lab.se_proj.ActivitySettings;
import com.se_lab.se_proj.FragmentDashboard;
import com.se_lab.se_proj.FragmentHelp;
import com.se_lab.se_proj.HealthMgmt.ActivityHealthMgmt;
import com.se_lab.se_proj.Nearby.ActivityNearby;
import com.se_lab.se_proj.R;
import com.se_lab.se_proj.databinding.ActivityHealthMgmtBinding;
import com.se_lab.se_proj.databinding.ActivityWithNavBinding;

public abstract class BaseNavigationActivity extends AppCompatActivity {

    ActivityWithNavBinding binding;

    ActionBarDrawerToggle actionBarDrawerToggle;

    public abstract void setupPresenter();

    public abstract BaseNavigationActivityPresenter getActivityPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_with_nav);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.drawer_open,
                R.string.drawer_close);
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        final NavigationView navView = (NavigationView) binding.navDrawer.findViewById(R.id.nav_drawer_menu_view);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                switch (item.getItemId()){
                    case R.id.menu_dashboard:
                        getActivityPresenter().onDashBoardClicked();
                        break;
                    case R.id.menu_healthmgmt:
                        getActivityPresenter().onHealthMgmtClicked();
                        break;
                    case R.id.menu_nearby:
                        getActivityPresenter().onNearbyClicked();
                        break;
                    case R.id.menu_profile:
                        getActivityPresenter().onProfileClicked();
                        break;
                    case R.id.menu_help:
                        getActivityPresenter().onHelpClicked();
                        break;
                    case R.id.menu_logout:
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(BaseNavigationActivity.this, ActivityLoginPage.class);
                        startActivity(intent);
                        finish();
                }
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        // Pass the event to ActionBarDrawerToggle, if it returns
//        // true, then it has handled the app icon touch event
//        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
//        // Handle your other action bar items...
        switch (item.getItemId()){
            case android.R.id.home:
                actionBarDrawerToggle.onOptionsItemSelected(item);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDashboard(){
        Intent intent = new Intent(this, ActivityMain.class);
        startActivity(intent);
    }

    public void showHealthMgmt(){
        Intent intent = new Intent(this, ActivityHealthMgmt.class);
        startActivity(intent);
    }

    public void showHelp(){
        Intent intent = new Intent(this, ActivityHelp.class);
        startActivity(intent);
    }

    public void showProfile(){
        Intent intent = new Intent(this, ActivitySettings.class);
        startActivity(intent);
    }

    public void showNearby(){
        Intent intent = new Intent(this, ActivityNearby.class);
        startActivity(intent);
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
