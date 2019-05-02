package com.se_lab.se_proj;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

public class FragmentHelp extends Fragment {

    View view;
    TextView tv_help;
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_help, container, false);

        tv_help = (TextView) view.findViewById(R.id.tv_help);

        getHelpMenu();

        return view;
    }

    public void getHelpMenu(){
        tv_help.setText("hello");
        tv_help.setText("\n1.  Is my phone supported?\n" +
                "\n" +
                "Our Mobile Apps are supported on the following platforms:\n" +
                "\n" +
                "Android 2.2\n" +
                "Android 2.3\n" +
                "Android 2.3.2\n" +
                "Android 2.3.3\n" +
                "\n" +
                "\n" +
                "2.  What features does the Mobile App have?\n" +
                "\n" +
                "The Mobile App give you the ability to you to access your account information, keep track of your daily steps, calories, etc.\n" +
                "\n" +
                "\n" +
                "For any other information, Please email to developer@gmail.com\n"
        );
    }
}
