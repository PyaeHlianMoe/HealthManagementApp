package com.se_lab.se_proj.HealthMgmt.addfood;

import com.se_lab.se_proj.BasePresenter;
import com.se_lab.se_proj.BaseView;

/**
 * Contract between AddActivity View, Presenter and Model
 */

interface AddActivityContract {
    interface View extends BaseView<Presenter> {

        void showProducts();

        void showAddFoodDialog();

    }

    interface Presenter extends BasePresenter {

        void onIntentLoaded(int mealNumber);

    }

    interface Model {

        void setMealId(int mealId);

    }

}