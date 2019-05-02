package com.se_lab.se_proj.HealthMgmt.DailyProgress;

import android.support.annotation.NonNull;

import com.se_lab.se_proj.HealthMgmt.HealthModel;

class DailyProgressPresenter implements DailyProgressContract.Presenter {

    @NonNull
    private HealthModel mActivityModel;

    @NonNull
    private DailyProgressContract.View mDailyMealsView;

    DailyProgressPresenter(@NonNull HealthModel model,
                           @NonNull DailyProgressContract.View view) {
        mDailyMealsView = view;
        mActivityModel = model;

        mDailyMealsView.setPresenter(this);
    }

    @Override
    public void start() {

    }


    @Override
    public void getDate() {

    }

}