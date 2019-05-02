package com.se_lab.se_proj.Profile;

import com.google.firebase.firestore.DocumentReference;
import com.se_lab.se_proj.ActivityMain;
import com.se_lab.se_proj.ActivityLoginPage;
import com.se_lab.se_proj.model.Profile;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.se_lab.se_proj.R;
import com.google.firebase.firestore.DocumentSnapshot;
import android.support.annotation.NonNull;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import java.util.List;
import com.google.firebase.firestore.QuerySnapshot;

import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

public class FragmentSettings extends Fragment implements View.OnClickListener{

    View view;

    //private Button buttonlogout;
    private FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    RadioButton buttonMale,buttonFemale,buttonGetFit,buttonLoseWeight, buttonGainMuscle;
    String gender,year,height,weight,tweight,firstName,lastName,goal;
    Button saveButton;
    String documentId = "";
    String userId;
    Profile c;

    EditText editText_year, editText_height, editText_cWeight,editText_tWeight, editText_firstName,editText_lastName;
    TextView tv_BMI;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container,false);

        db = FirebaseFirestore.getInstance();

        saveButton = view.findViewById(R.id.saveButton);
        buttonMale = view.findViewById(R.id.genderRadioGroup_male);
        buttonFemale = view.findViewById(R.id.genderRadioGroup_female);


        buttonGetFit = view.findViewById(R.id.program_getfit);
        buttonGainMuscle = view.findViewById(R.id.program_gainmuscle);
        buttonLoseWeight = view.findViewById(R.id.program_loseweight);

        editText_year = (EditText)view.findViewById(R.id.editText_dob);
        editText_height = (EditText)view.findViewById(R.id.editText_height);
        editText_cWeight = (EditText)view.findViewById(R.id.editText_currentWeight);
        editText_tWeight = (EditText)view.findViewById(R.id.editText_targetWeight);
        editText_firstName = (EditText)view.findViewById(R.id.editText_firstName);
        editText_lastName = (EditText)view.findViewById(R.id.editText_lastName);

        tv_BMI = (TextView)view.findViewById(R.id.tv_BMI);


        saveButton.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
        //buttonlogout.setOnClickListener(this);

        if(firebaseAuth.getCurrentUser() == null){
            //profile activity here
            Toast.makeText(getActivity(), "Please log in....", Toast.LENGTH_LONG).show();
            getActivity().finish();
            startActivity(new Intent(getActivity(), ActivityLoginPage.class));
        }
        else {
            QuerySettings();
        }

        return view;
    }

    //@Override
    //public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
     //   ((ActivityMain)getActivity()).getMenuInflater().inflate(R.menu.main_menu, menu);
     //   return true;
    //}

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

    @Override
    public void onClick(View view) {
        if(view == saveButton) {
            updateProfile();
        }
    }

    public void QuerySettings() {

        // replace with 'username' instead of 'God'
        userId = firebaseAuth.getCurrentUser().getUid();
        Log.d("test", userId);
        documentId = userId;
        //FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("userprofile")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if(!queryDocumentSnapshots.isEmpty()){
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for(DocumentSnapshot d : list) {
                                documentId = d.getId();
                                Log.d("test", documentId);
                            }
                        }
                    }
                });

        Toast.makeText(getActivity(), "Getting user information..." , Toast.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (documentId.equals("") == false){
                    db.collection("userprofile").document(documentId)
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                gender = document.getString("gender");
                                height = document.getString("height");
                                weight = document.getString("weight");
                                tweight = document.getString("targetWeight");
                                year = document.getString("year");
                                firstName = document.getString("firstName");
                                lastName = document.getString("lastName");
                                goal = document.getString("goal");

                                if (gender == "male") {
                                    buttonMale.setChecked(true);
                                } else {
                                    buttonFemale.setChecked(true);
                                }

                                if (goal.toUpperCase().equals("GET FIT")) {
                                    buttonGetFit.setChecked(true);
                                } else if  (goal.toUpperCase().equals("GAIN MUSCLE")) {
                                    buttonGainMuscle.setChecked(true);
                                } else {
                                    buttonLoseWeight.setChecked(true);
                                }


                                editText_year.setText(year);
                                editText_cWeight.setText(weight);
                                editText_tWeight.setText(tweight);
                                editText_height.setText(height);
                                editText_firstName.setText(firstName);
                                editText_lastName.setText(lastName);

                                Integer intHeight;
                                if (height.equals("")) {
                                    intHeight = 1 ;
                                }
                                else{
                                    intHeight = Integer.parseInt(height);
                                }
                                Integer intWeight;
                                if (weight.equals("")) {
                                    intWeight = 0 ;
                                }
                                else{
                                    intWeight = Integer.parseInt(weight);
                                }
                                Log.d("height", String.valueOf(intHeight));
                                Log.d("weight", String.valueOf(intWeight));
                                double bmi;
                                bmi = (intWeight*10000) / (intHeight * intHeight);
                                Log.d("bmi ", String.valueOf(bmi));

                                tv_BMI.setText("BMI : " + String.valueOf(bmi));
                            }
                        }
                    });
            }
            }
        },3000);


    }


    private void updateProfile() {
        userId = firebaseAuth.getCurrentUser().getUid();

        if (buttonFemale.isChecked() == true) {
            gender = "female";
        } else {
            gender = "male";
        }

        if (buttonGetFit.isChecked() == true) {
            goal = "Get Fit";
        } else if (buttonGainMuscle.isChecked() == true) {
            goal = "Gain Muscle";
        } else {
            goal = "Lose Weight";
        }


        height = editText_height.getText().toString();
        weight = editText_cWeight.getText().toString();
        tweight = editText_tWeight.getText().toString();
        firstName = editText_firstName.getText().toString();
        lastName = editText_lastName.getText().toString();
        year = editText_year.getText().toString();


        if (!height.equals("") && Integer.parseInt(height) >= 50 && Integer.parseInt(height) <= 250)
        {
            if (!weight.equals("") && Integer.parseInt(weight) >= 5 && Integer.parseInt(weight) <= 250) {
                if ((!tweight.equals("") && Integer.parseInt(tweight)>=5 &&  Integer.parseInt(tweight) <= 250) || tweight.equals("")) {
                    //Toast.makeText(getActivity(), program, Toast.LENGTH_LONG).show();
                    c = new Profile(
                            firstName, lastName, year, gender, String.valueOf(weight), String.valueOf(height), String.valueOf(tweight), goal
                    );


                    if (!documentId.equals("")) {
                        db.collection("userprofile").document(documentId)
                                .set(c)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_LONG).show();
                                    }
                                });
                    } else {
                        db.collection("userprofile").
                                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    //QuerySnapshot querySnapshot = task.getResult();
                                    //final int count = querySnapshot.size() + 1;
                                    final DocumentReference documentReference = db.collection("userprofile").document();
                                    db.collection("userprofile").document(userId).set(c)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    db.collection("userprofile").document(documentReference.getId())
                                                            .update("profileId", documentReference.getId());
                                                    Toast.makeText(getActivity(), "New Profile added", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                }
                            }
                        });
                    }
                }
            }
            else
            {
                Toast.makeText(getActivity(), "Please enter valid weight value.", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(getActivity(), "Please enter valid height value.", Toast.LENGTH_LONG).show();
        }
    }
}
