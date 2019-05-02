package com.se_lab.se_proj.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.se_lab.se_proj.ActivityMain;
import com.se_lab.se_proj.ActivityProfile;
import com.se_lab.se_proj.FragmentDashboard;
import com.se_lab.se_proj.R;


public class FragmentProfileProgram extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile_program, container, false);

        Button button_getFit = view.findViewById(R.id.button_getFit);
        button_getFit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = getArguments();
                bundle.putString("goal","get fit");

                ((ActivityProfile)getActivity()).loadFragmentWithBundle(new FragmentProfileUsername(),bundle);
            }
        });

        Button button_gainMuscle = view.findViewById(R.id.button_gainMuscle);
        button_gainMuscle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = getArguments();
                bundle.putString("goal","gain Muscle");

                ((ActivityProfile)getActivity()).loadFragmentWithBundle(new FragmentProfileUsername(),bundle);
            }
        });

        Button button_loseWeight = view.findViewById(R.id.button_loseWeight);
        button_loseWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = getArguments();
                bundle.putString("goal","lose weight");

                ((ActivityProfile)getActivity()).loadFragmentWithBundle(new FragmentProfileUsername(),bundle);
            }
        });

        // GOTO: dashboard if user Skip the personalization
        Button profileSkipButton = view.findViewById(R.id.profileSkipButton);
        profileSkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityMain.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
