package com.se_lab.se_proj.HealthMgmt.diet.time;

import com.se_lab.se_proj.BasePresenter;
import com.se_lab.se_proj.BaseView;
import com.se_lab.se_proj.model.BaseMeal;

import java.util.ArrayList;

/**
 * Contract between TimeFragment View and Presenter
 */

public interface TimeContract {

    interface View extends BaseView<Presenter> {

        void updateMealsView(ArrayList<BaseMeal> meals);

        void updateMeal(int position, BaseMeal meal);

        void showTimePicker(int position,
                            int currentHour, int currentMinutes,
                            int afterHour, int afterMinutes,
                            int beforeHour, int beforeMinutes);

        void showNextFragment();

        void showPreviousFragment();

        void addMealToList(int position);

        void removeMealFromList(int position);

    }

    interface Presenter extends BasePresenter {

        void onBackClicked();

        void onNextClicked();

        void onDestroy();

        void getMeals();

        void onMealTimeClicked(int position);
    }

    interface Model {

        void getMeal(GetMealCallback callback, int position);

        void getNeighbouringMealsTime(GetMealsTimeCallback callback, int position);

        interface GetMealCallback {

            void onMealLoaded(int position, BaseMeal meal);

        }

        interface GetMealsTimeCallback{
            void onMealsTimeLoaded(int currentHour, int currentMinutes,
                                   int afterHour, int afterMinutes,
                                   int beforeHour, int beforeMinutes);
        }

    }
}

