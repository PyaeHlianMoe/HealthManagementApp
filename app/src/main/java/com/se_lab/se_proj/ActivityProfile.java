package com.se_lab.se_proj;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.se_lab.se_proj.Profile.FragmentProfileNew;


public class ActivityProfile extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    static boolean isUserLogged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(mAuth.getCurrentUser() != null){
            setContentView(R.layout.splash_screen);
            RelativeLayout movingBackground = findViewById(R.id.moving_background);
            AnimationDrawable animationDrawable = (AnimationDrawable) movingBackground.getBackground();
            animationDrawable.setEnterFadeDuration(1500);
            animationDrawable.setExitFadeDuration(1500);
            animationDrawable.start();

            startWithSplashLogin(mAuth.getCurrentUser().getUid());
        } else {
            //user not in registered yet
            startWithoutSplashLogin();
        }
    }

    private void startWithSplashLogin(String userId){
        CollectionReference userReference = db.collection("userprofile");
        Query query = userReference.whereEqualTo("userId",userId);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    QuerySnapshot queryResult = task.getResult();
                    if(!queryResult.isEmpty()){
                        isUserLogged = true;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(ActivityProfile.this, ActivityMain.class);
                                startActivity(intent);
                            }
                        },4000);
                    } else {
                        //user is in firebaseAuth but no record in DB
                        isUserLogged = true;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setContentView(R.layout.activity_profile);
                                loadFragment(new FragmentProfileNew());
                            }
                        }, 2000);
                    }
                } else {
                    isUserLogged = false;
                    startWithoutSplashLogin();
                }

            }
        });
    }

    private void startWithoutSplashLogin(){
        setContentView(R.layout.activity_profile);
        loadFragment(new FragmentProfileNew());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadFragment(Fragment fragment) {
        // create a FragmentManager
        FragmentManager fm = getSupportFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.profileFrameLayout, fragment);
        fragmentTransaction.commit(); // save the changes
    }

    public void loadFragmentWithBundle(Fragment fragment, Bundle bundle) {
        Fragment fragmentReceived = fragment;
        Bundle bundleReceived = bundle;
        FragmentManager fm = getSupportFragmentManager();
        fragmentReceived.setArguments(bundleReceived);
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.profileFrameLayout, fragmentReceived);
        fragmentTransaction.commit();
    }

}
