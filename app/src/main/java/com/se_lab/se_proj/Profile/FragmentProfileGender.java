package com.se_lab.se_proj.Profile;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.se_lab.se_proj.ActivityLoginPage;
import com.se_lab.se_proj.ActivityMain;
import com.se_lab.se_proj.ActivityProfile;
import com.se_lab.se_proj.FragmentDashboard;
import com.se_lab.se_proj.R;

public class FragmentProfileGender extends Fragment {

    private Button buttonMale;
    private Button buttonFemale;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile_gender, container, false);


        //get Gender
        buttonMale = view.findViewById(R.id.maleButton);
        buttonFemale = view.findViewById(R.id.femaleButton);

        buttonMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("gender","male");

                ((ActivityProfile)getActivity()).loadFragmentWithBundle(new FragmentProfileYear(),bundle);
            }
        });

        buttonFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("gender","female");

                ((ActivityProfile)getActivity()).loadFragmentWithBundle(new FragmentProfileYear(),bundle);
            }
        });

        Button profileSkipBtn = view.findViewById(R.id.profileSkipButton);
        profileSkipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityMain.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
