package com.se_lab.se_proj.Profile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.se_lab.se_proj.ActivityMain;
import com.se_lab.se_proj.ActivityProfile;
import com.se_lab.se_proj.FragmentDashboard;
import com.se_lab.se_proj.R;


public class FragmentProfileYear extends Fragment {

    View view;
    TextView mDisplayDate;
    DatePickerDialog.OnDateSetListener mDatePickerDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile_year, container, false);

        final TextView yearOfBornEditTxt = view.findViewById(R.id.yearOfBornTextView);
        yearOfBornEditTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new FragmentDatePicker();
                newFragment.show(getFragmentManager(),"DatePicker");

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

        Button profileNextButton = view.findViewById(R.id.profileYearNextButton);
        profileNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String yearOfBorn = yearOfBornEditTxt.getText().toString().trim();
                if(TextUtils.isEmpty(yearOfBorn)){
                    Toast.makeText(getActivity(),"Please enter Year of Birth",Toast.LENGTH_SHORT).show();
                } else {
                    Bundle bundle = getArguments();
                    bundle.putString("year", yearOfBorn);

                    ((ActivityProfile)getActivity()).loadFragmentWithBundle(new FragmentProfileHeight(),bundle);
                }
            }
        });
        return view;
    }


//    @Override
//    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.YEAR,year);
//        calendar.set(Calendar.MONTH,month);
//        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
//
//        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
//        EditText yearOfBornEditTxt = view.findViewById(R.id.yearOfBornEditText);
//        yearOfBornEditTxt.setText(currentDateString);
//    }
}

