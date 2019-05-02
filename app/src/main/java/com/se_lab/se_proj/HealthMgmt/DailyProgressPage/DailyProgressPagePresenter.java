package com.se_lab.se_proj.HealthMgmt.DailyProgressPage;

import android.support.annotation.NonNull;

import com.se_lab.se_proj.HealthMgmt.HealthModel;
import com.se_lab.se_proj.model.DietProgress;

public class DailyProgressPagePresenter implements DailyProgressPageContract.Presenter {

    @NonNull
    private HealthModel mActivityModel;

    @NonNull
    private DailyProgressPageContract.View mDailyMealsView;

    DailyProgressPagePresenter(@NonNull HealthModel model,
                               @NonNull DailyProgressPageContract.View view) {
        mDailyMealsView = view;
        mActivityModel = model;

        mDailyMealsView.setPresenter(this);
    }

    @Override
    public void start() {

    }


    @Override
    public void getDietProgress(long date) {
        DailyProgressPageContract.Model.GetDietProgressCallback callback =
                new DailyProgressPageContract.Model.GetDietProgressCallback() {
                    @Override
                    public void onDietProgressLoaded(DietProgress dietProgress) {
                        mDailyMealsView.showDailyDietProgress(dietProgress);
                    }
                };

        mActivityModel.getDietProgress(callback, date);
    }

    @Override
    public void onDateLoaded(long date) {
        getDietProgress(date);
    }

    @Override
    public void onAddFoodClicked(int mealNumber) {
        mDailyMealsView.showAddFood(mealNumber);
    }
}
