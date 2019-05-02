package com.se_lab.se_proj;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Arrays;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import org.json.JSONObject;
import org.json.JSONException;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.se_lab.se_proj.Profile.FragmentSettings;
import android.util.Log;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;
import android.widget.TextView;

public class ActivityLoginPage extends AppCompatActivity implements View.OnClickListener{

    private Button buttonLogIn;
    private Button forgetPassword;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private String Name;
    private String FEmail;
    CallbackManager callbackManager;
    LoginButton loginButtonFB;

    private TextView btn_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        callbackManager = CallbackManager.Factory.create();
        loginButtonFB = (LoginButton) findViewById(R.id.button_login_fb);
        forgetPassword = findViewById(R.id.button_forgetPassword);
        btn_signup = (TextView) findViewById(R.id.btn_signup);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){ //user alr log in
            //profile activity here
            finish();
            startActivity(new Intent(getApplicationContext(), ActivityMain.class));
        }

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonLogIn = findViewById(R.id.logInButton);

        loginButtonFB.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));

        buttonLogIn.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);


        LoginManager.getInstance().logOut();
        loginButtonFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithFB();
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLoginPage.this, ActivityProfile.class);
                startActivity(intent);
                finish();
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLoginPage.this, ActivityForgetPassword.class);
                startActivity(intent);
            }
        });

    }

    private void loginWithFB(){
        // Callback registration

        //mCallbackManager = CallbackManager.Factory.create();
        //LoginButton loginButton = findViewById(R.id.buttonFacebookLogin);
        loginButtonFB.setReadPermissions("email", "public_profile");
        loginButtonFB.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("test", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("test", "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("test", "facebook:onError", error);
                // ...
            }
        });
// ...
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("test", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("test", "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            //updateUI(user);
                            finish();
                            startActivity(new Intent(getApplicationContext(), ActivityMain.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("test", "signInWithCredential:failure", task.getException());
                            Toast.makeText(ActivityLoginPage.this, "Authentication failed,  Please register first.",
                                    Toast.LENGTH_SHORT).show();

                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void userLogIn(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this,"Please enter email",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            //pw is empty
            Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(), ActivityMain.class));
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onClick(View view){
        if(view == buttonLogIn) {
            userLogIn();
        }
        if(view == loginButtonFB) {
            loginWithFB();
        }
        if(view == btn_signup){

        }
    }
}
