package com.se_lab.se_proj.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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


public class FragmentProfileHeight extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile_height, container, false);
        final EditText cmEditTxt = view.findViewById(R.id.cmEditText);
        final TextView cmTxtView = view.findViewById(R.id.cmTextView);
        final EditText feetEditTxt1 = view.findViewById(R.id.feetEditText1);
        final TextView feetTxtView1 = view.findViewById(R.id.feetTextView1);
        final EditText feetEditTxt2 = view.findViewById(R.id.feetEditText2);
        final TextView feetTxtView2 = view.findViewById(R.id.feetTextView2);
        final RadioButton radioButtonCm = view.findViewById(R.id.heightRadioButton_cm);
        final RadioButton radioButtonFeet = view.findViewById(R.id.heightRadioButton_feet);
        final RadioGroup heightRadioGrp = view.findViewById(R.id.heightRadioGroup);


        radioButtonCm.setVisibility(View.INVISIBLE);
        radioButtonFeet.setVisibility(View.INVISIBLE);
        radioButtonCm.setChecked(true);

        heightRadioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.heightRadioButton_cm:
                        cmEditTxt.setVisibility(View.VISIBLE);
                        cmTxtView.setVisibility(View.VISIBLE);
                        feetEditTxt1.setVisibility(View.INVISIBLE);
                        feetTxtView1.setVisibility(View.INVISIBLE);
                        feetEditTxt2.setVisibility(View.INVISIBLE);
                        feetTxtView2.setVisibility(View.INVISIBLE);
                        if((!TextUtils.isEmpty(feetEditTxt1.getText().toString()))&&(!TextUtils.isEmpty(feetEditTxt2.getText().toString()))){
                            double feetEditTxt1Content = Integer.parseInt(feetEditTxt1.getText().toString());
                            double feetEditTxt2Content = (Integer.parseInt(feetEditTxt2.getText().toString()))/100.00;
                            double feetTotal = feetEditTxt1Content + feetEditTxt2Content;
                            cmEditTxt.setText(feetToCm(feetTotal));
                        }

                        break;
                    case R.id.heightRadioButton_feet:
                        cmEditTxt.setVisibility(View.INVISIBLE);
                        cmTxtView.setVisibility(View.INVISIBLE);
                        feetEditTxt1.setVisibility(View.VISIBLE);
                        feetTxtView1.setVisibility(View.VISIBLE);
                        feetEditTxt2.setVisibility(View.VISIBLE);
                        feetTxtView2.setVisibility(View.VISIBLE);
                        if(!TextUtils.isEmpty(cmEditTxt.getText().toString())){
                            //need to handle illegal input
                            //
                            //
                            //
                            int cmEditTxtContent = Integer.parseInt(cmEditTxt.getText().toString());
                            String[] feetresult = String.valueOf(cmToFeet(cmEditTxtContent)).split("\\.");
                            feetEditTxt1.setText(feetresult[0]);
                            feetEditTxt2.setText(feetresult[1]);
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

        Button profileNextButton = view.findViewById(R.id.profileHeightNextButton);
        profileNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(heightRadioGrp.getCheckedRadioButtonId()==-1){
                    Toast.makeText(getActivity(),"Please enter your height",Toast.LENGTH_SHORT).show();
                }

                if(radioButtonCm.isChecked()){
                    String cmHeight = cmEditTxt.getText().toString().trim();
                    if(TextUtils.isEmpty(cmHeight)){
                        Toast.makeText(getActivity(),"Please enter your height",Toast.LENGTH_SHORT).show();
                    } else {
                        int intCmHeight = Integer.parseInt(cmHeight);
                        if(intCmHeight >= 50 && intCmHeight <=250 ){
                            Bundle bundle = getArguments();
                            bundle.putString("height", cmHeight);
                            ((ActivityProfile)getActivity()).loadFragmentWithBundle(new FragmentProfileWeight(),bundle);
                        } else {
                            Toast.makeText(getActivity(),"Please enter a proper height",Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                if (radioButtonFeet.isChecked()){
                    String feetHeight1 = feetEditTxt1.getText().toString().trim();
                    String feetHeight2 = feetEditTxt2.getText().toString().trim();
                    if((TextUtils.isEmpty(feetHeight1))||(TextUtils.isEmpty(feetHeight2))){
                        Toast.makeText(getActivity(),"Please fully enter your feet and inches",Toast.LENGTH_SHORT).show();
                    } else {
                        int intFeetHeight1 = Integer.parseInt(feetHeight1);
                        int intFeetHeight2 = Integer.parseInt(feetHeight2);
                        if(intFeetHeight1 > 1 || intFeetHeight2 > 0 ){
                            Bundle bundle = getArguments();
                            bundle.putString("height", feetHeight1 + "." + feetHeight2 + "ft");
                            ((ActivityProfile)getActivity()).loadFragmentWithBundle(new FragmentProfileWeight(),bundle);
                        } else {
                            Toast.makeText(getActivity(),"Please enter a proper height",Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        });
        return view;
    }

    static String cmToFeet(int cmheight){
        DecimalFormat df2 = new DecimalFormat(".##");
        return df2.format(cmheight / 30.48);
    }

    static String feetToCm(double feetheight){
        DecimalFormat df2 = new DecimalFormat("0");
        return df2.format(feetheight * 30.48);
    }
}

