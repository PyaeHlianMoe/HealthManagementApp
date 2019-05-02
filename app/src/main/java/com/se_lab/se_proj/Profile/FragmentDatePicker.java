package com.se_lab.se_proj.Profile;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.se_lab.se_proj.Profile.FragmentProfileGender;
import com.se_lab.se_proj.R;

import java.util.Calendar;
import java.util.Date;

public class FragmentDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    DatePickerDialog dpd;
    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                AlertDialog.THEME_HOLO_LIGHT,this,year,month,day);
        datepickerdialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        hide(datepickerdialog.getDatePicker());

        return datepickerdialog;
    }

    private void hide(DatePicker mDatePicker) {
        try {
            int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
            int monthSpinnerId = Resources.getSystem().getIdentifier("month", "id", "android");

            if (daySpinnerId != 0) {
                View daySpinner = mDatePicker.findViewById(daySpinnerId);
                if (daySpinner != null)
                    daySpinner.setVisibility(View.GONE);
            }

            if (monthSpinnerId != 0) {
                View monthSpinner = mDatePicker.findViewById(monthSpinnerId);
                if (monthSpinner != null)
                    monthSpinner.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        TextView dob = getActivity(). findViewById(R.id.yearOfBornTextView);

        dob.setText(String.valueOf(year));

    }
}
