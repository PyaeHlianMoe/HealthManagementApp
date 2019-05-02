package com.se_lab.se_proj.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.se_lab.se_proj.ActivityLoginPage;
import com.se_lab.se_proj.ActivityMain;
import com.se_lab.se_proj.ActivityProfile;
import com.se_lab.se_proj.R;

public class FragmentProfileNew extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile_new, container, false);

        Button btn_new_user = view.findViewById(R.id.btn_new_user);
        Button btn_old_user = view.findViewById(R.id.btn_old_user);

        btn_new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String activityClassName = getActivity().getClass().getSimpleName();
                if (activityClassName.equals("ActivityProfile")){
                    ((ActivityProfile)getActivity()).loadFragment(new FragmentProfileGender());
                }
                if (activityClassName.equals("ActivityMain")){
                    ((ActivityMain)getActivity()).loadFragment(new FragmentProfileGender());
                }

            }
        });

        btn_old_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(), ActivityLoginPage.class);
                //startActivity(intent);

                //String activityClassName = getActivity().getClass().getSimpleName();
                getActivity().finish();
                startActivity(new Intent(getActivity(), ActivityLoginPage.class));

            }
        });

        return view;
    }

}
