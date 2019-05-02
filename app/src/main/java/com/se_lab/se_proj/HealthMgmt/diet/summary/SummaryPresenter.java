package com.se_lab.se_proj.HealthMgmt.diet.summary;

import android.support.annotation.NonNull;

import com.se_lab.se_proj.HealthMgmt.diet.DietModel;
import com.se_lab.se_proj.model.Diet;

/**
 * Implementation of SummaryFragment presenter
 */

class SummaryPresenter implements SummaryContract.Presenter {

    @NonNull
    private DietModel mActivityModel;

    @NonNull
    private SummaryContract.View mSummaryView;

    SummaryPresenter(@NonNull DietModel model,
                     @NonNull SummaryContract.View view) {
        mSummaryView = view;
        mActivityModel = model;

        mSummaryView.setPresenter(this);
    }

    @Override
    public void start() {
        getStartValues();
    }

    @Override
    public void getStartValues() {
        mActivityModel.getDietSummary(new SummaryContract.Model.GetDietSummaryCallback() {
            @Override
            public void onSummaryLoaded(Diet diet) {
                mSummaryView.updateView(diet);
            }
        });
    }

    @Override
    public void onBackClicked() {
        mSummaryView.showPreviousFragment();
    }

    @Override
    public void onSaveClicked() {
        mSummaryView.saveDiet();
    }
}
