package com.se_lab.se_proj.HealthMgmt.diet.general;

import com.se_lab.se_proj.BasePresenter;
import com.se_lab.se_proj.BaseView;

/**
 * Contract between GeneralFragment View and Presenter
 */

public interface GeneralContract {

    interface View extends BaseView<GeneralPresenter> {

        void showStartValues(int caloriesLimit, int mealsNumber);

        void showCaloriesError();

        void hideCaloriesError();

        void hideSoftInput();

        void showNextFragment();

    }

    interface Presenter extends BasePresenter {

        void getStartValues();

        void onMealsNumberChanged(int mealsNumber);

        void onCaloriesLimitChanged(String caloriesLimit);

        void onNextClicked();

    }

    interface Model {

        boolean isCaloriesLimitProper(String caloriesLimitInput);

        void setCaloriesLimit(int caloriesLimit);

        void setMealsNumber(int mealsNumber);

        void getGeneralValues(GetGeneralValuesCallback callback);

        interface GetGeneralValuesCallback {

            void onGeneralValuesLoaded(int caloriesLimit, int mealsNumber);

        }

    }
}
