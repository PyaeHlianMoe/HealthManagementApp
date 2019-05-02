package com.se_lab.se_proj.HealthMgmt;

import android.support.annotation.NonNull;

import com.se_lab.se_proj.model.Diet;

public class HealthPresenter implements HealthContract.Presenter{

    @NonNull
    private HealthContract.Model mActivityModel;

    @NonNull
    private HealthContract.View mActivityView;

    HealthPresenter(@NonNull HealthContract.Model model,
                  @NonNull HealthContract.View view) {
        mActivityView = view;
        mActivityModel = model;
        mActivityView.setPresenter(this);
    }

    @Override
    public void start() {
        getDiet();
    }

    @Override
    public void getDiet() {
        mActivityModel.getDiet(new HealthContract.Model.GetDietCallback() {
            @Override
            public void onDietLoaded(Diet diet) {
                mActivityView.showDailyMeals(diet);
            }

            @Override
            public void onDietNotSet() {
                mActivityView.showNewDiet();
            }
        });
    }

    @Override
    public void onDashBoardClicked() {
        mActivityView.showDashboard();
    }

    @Override
    public void onHealthMgmtClicked() {
        mActivityView.showHealthMgmt();
    }

    @Override
    public void onHelpClicked() {
        mActivityView.showHelp();
    }

    @Override
    public void onProfileClicked() {
        mActivityView.showProfile();
    }

    @Override
    public void onNearbyClicked() {
        mActivityView.showNearby();
    }
}
