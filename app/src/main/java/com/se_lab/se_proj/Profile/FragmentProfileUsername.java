package com.se_lab.se_proj.Profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.se_lab.se_proj.ActivityMain;
import com.se_lab.se_proj.ActivityProfile;
import com.se_lab.se_proj.FragmentDashboard;
import com.se_lab.se_proj.R;

import java.util.HashMap;
import java.util.Map;

public class FragmentProfileUsername extends Fragment {

    View view;

    private Button createButton;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private FirebaseAuth firebaseAuth;
    private EditText editTextUsername;
    private ProgressDialog progressDialog;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile_username, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        createButton = view.findViewById(R.id.createButton);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        editTextUsername = view.findViewById(R.id.editTextUsername);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        progressDialog = new ProgressDialog(getActivity());
        return view;
    }

    private void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(getActivity(),"Please enter email",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            //pw is empty
            Toast.makeText(getActivity(),"Please enter password",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(username)){
            //username is empty
            Toast.makeText(getActivity(),"Please enter username",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        //if validation is ok
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //user is successfully registered and logged in
                            //we will start the profile activity here
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Registered Successfully",Toast.LENGTH_SHORT).show();
                            addNewProfile();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(),"Could not register, please try again",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void addNewProfile(){

        String username = editTextUsername.getText().toString().trim();
        Bundle bundle = getArguments();
        String gender = bundle.getString("gender");
        String year = bundle.getString("year");
        String weight = bundle.getString("weight");
        String height = bundle.getString("height");
        String goal = bundle.getString("goal");
        final String userid = firebaseAuth.getCurrentUser().getUid();

        final Map<String, Object> profile = new HashMap<>();

        profile.put("username",username);
        profile.put("firstName",username);
        profile.put("lastName",username);
        profile.put("gender",gender);
        profile.put("year", year);
        profile.put("weight", weight);
        profile.put("height", height);
        profile.put("goal", goal);
        profile.put("userId",userid);

        progressDialog.setMessage("Adding New User...");
        progressDialog.show();

        db.collection("userprofile").
                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    //QuerySnapshot querySnapshot = task.getResult();
                    //final int count = querySnapshot.size() + 1;
                    final DocumentReference documentReference = db.collection("userprofile").document();
                    db.collection("userprofile").document(userid).set(profile)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    db.collection("userprofile").document(documentReference.getId())
                                            .update("profileId",documentReference.getId());
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(getActivity(), ActivityMain.class);
                                    startActivity(intent);
                                }
                            });
                }
            }
        });
    }
}
