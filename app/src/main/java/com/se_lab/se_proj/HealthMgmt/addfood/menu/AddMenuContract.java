package com.se_lab.se_proj.HealthMgmt.addfood.menu;

import com.se_lab.se_proj.BasePresenter;
import com.se_lab.se_proj.BaseView;
import com.se_lab.se_proj.model.Food;

import java.util.List;

/**
 * Contract class between AddMenuFragment's View and Presenter
 */
public interface AddMenuContract {
    interface View extends BaseView<AddMenuContract.Presenter> {

        void showProducts();

        void showAddDialog();

        void updateFoods(List<Food> foods);

    }

    interface Presenter extends BasePresenter {

        void onProductsClicked();

        void onFoodClicked(Food food);

    }

    interface Model {

        void getRecentlyAddedFood(GetRecentlyAddedFoodCallback callback);

        interface GetRecentlyAddedFoodCallback {
            void onFoodLoaded(List<Food> foods);
        }

        void setDialogFoodCache(Food food);

        void setDialogMode(boolean isInEditMode);

    }

}
