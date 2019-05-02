package com.se_lab.se_proj.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.se_lab.se_proj.ActivityMain;
import com.se_lab.se_proj.ActivityProfile;
import com.se_lab.se_proj.FragmentDashboard;
import com.se_lab.se_proj.R;

import java.text.DecimalFormat;


public class FragmentProfileWeight extends Fragment {

    View view;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile_weight, container, false);
        final EditText weightEditTxt = view.findViewById(R.id.weightEditText);
        final TextView weightTxtView = view.findViewById(R.id.weightTextView);
        final RadioButton radioButtonKg = view.findViewById(R.id.weightRadioButton_kg);
        final RadioButton radioButtonLbs = view.findViewById(R.id.weightRadioButton_lbs);
        final RadioGroup weightRadioGrp = view.findViewById(R.id.weightRadioGroup);


        radioButtonKg.setVisibility(View.INVISIBLE);
        radioButtonLbs.setVisibility(View.INVISIBLE);
        radioButtonKg.setChecked(true);

        weightRadioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.weightRadioButton_kg:
                        weightTxtView.setText("kg");
                        if(!TextUtils.isEmpty(weightEditTxt.getText().toString())){
                            double weightLbsContent = Double.parseDouble(weightEditTxt.getText().toString());
                            weightEditTxt.setText(lbsToKg(weightLbsContent));
                        }
                        break;
                    case R.id.weightRadioButton_lbs:
                        weightTxtView.setText("lbs");
                        if(!TextUtils.isEmpty(weightEditTxt.getText().toString())){
                            double weightKgContent = Double.parseDouble(weightEditTxt.getText().toString());
                            weightEditTxt.setText(kgToLbs(weightKgContent));
                        }
                        break;
                }
            }
        });

        Button profileSkipBtn = view.findViewById(R.id.profileSkipButton);
        profileSkipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityMain.class);
                startActivity(intent);
            }
        });

        Button profileNextButton = view.findViewById(R.id.profileWeightNextButton);
        profileNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(weightRadioGrp.getCheckedRadioButtonId()==-1){
                    Toast.makeText(getActivity(),"Please enter your weight",Toast.LENGTH_SHORT).show();
                }

                if(radioButtonKg.isChecked()){
                    String kgWeight = weightEditTxt.getText().toString().trim();
                    if(TextUtils.isEmpty(kgWeight)){
                        Toast.makeText(getActivity(),"Please enter a proper weight",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        int kgInt = Integer.parseInt(kgWeight);
                        if (kgInt >= 5 && kgInt <= 250){
                            Bundle bundle = getArguments();
                            bundle.putString("weight", kgWeight);
                            ((ActivityProfile)getActivity()).loadFragmentWithBundle(new FragmentProfileProgram(),bundle);
                        } else {
                            Toast.makeText(getActivity(),"Please enter a proper weight",Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                if (radioButtonLbs.isChecked()){
                    String lbsWeight = weightEditTxt.getText().toString().trim();

                    if(TextUtils.isEmpty(lbsWeight)){
                        Toast.makeText(getActivity(),"Please enter a proper weight",Toast.LENGTH_SHORT).show();
                    } else {
                        int lbsInt = Integer.parseInt(lbsWeight);
                        if(lbsInt > 12){
                            Bundle bundle = getArguments();
                            bundle.putString("weight", lbsWeight);
                            ((ActivityProfile)getActivity()).loadFragmentWithBundle(new FragmentProfileProgram(),bundle);
                        } else {
                            Toast.makeText(getActivity(),"Please enter a proper weight",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        return view;
    }

    static String kgToLbs(Double kgWeight){
        DecimalFormat df2 = new DecimalFormat(".##");
        return df2.format(kgWeight * 2.205);
    }

    static String lbsToKg(Double lbsWeight){
        DecimalFormat df2 = new DecimalFormat(".##");
        return df2.format(lbsWeight / 2.205);
    }
}

