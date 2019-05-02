package com.se_lab.se_proj.HealthMgmt.DailyProgressPage;

import com.se_lab.se_proj.BasePresenter;
import com.se_lab.se_proj.BaseView;
import com.se_lab.se_proj.model.DietProgress;
import com.se_lab.se_proj.model.Meal;

public interface DailyProgressPageContract {

    interface View extends BaseView<Presenter> {

        void showDailyDietProgress(DietProgress data);

        void showAddFood(int mealNumber);

    }

    interface Presenter extends BasePresenter {

        void getDietProgress(long date);

        void onDateLoaded(long date);

        void onAddFoodClicked(int mealNumber);

    }

    interface Model {

        void getDietProgress(GetDietProgressCallback callback, long date);

        interface GetDietProgressCallback{
            void onDietProgressLoaded(DietProgress dietProgress);
        }

    }
}
