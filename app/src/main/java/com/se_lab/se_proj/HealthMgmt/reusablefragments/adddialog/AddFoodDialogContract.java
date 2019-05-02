package com.se_lab.se_proj.HealthMgmt.reusablefragments.adddialog;

import com.se_lab.se_proj.BasePresenter;
import com.se_lab.se_proj.BaseView;
import com.se_lab.se_proj.model.Food;

/**
 * Contract class between Model, View, Presenter of AddFoodDialog
 */

public interface AddFoodDialogContract {

    interface View extends BaseView<AddFoodDialogPresenter> {

        void notifySaveDismissed();

        void updateFoodValuesView(Food food, int foodWeight);

        void updateFoodData(Food food, int seekbarProgress);

        void setAddMode();

        void dismissDialog();

        interface DialogSaveDismissedCallback {

            void onDialogSaveDismissed();

        }

    }

    interface Presenter extends BasePresenter {

        void getFoodData();

        void onSeekBarProgressChanged(int progress);

        void onSaveClicked();

        void onDeleteClicked();

        void onCancelClicked();

    }

    interface Model {

        void getDialogFoodCache(GetDialogFoodCallback callback);

        interface GetDialogFoodCallback {

            void onFoodLoaded(Food food, int seekbarProgress, boolean isInEditMode);
        }

        void getUpdatedWeightValuesPerProgress(GetUpdatedWeightValuesCallback callback, int progress);

        interface GetUpdatedWeightValuesCallback {

            void onValuesUpdatedCounted(Food food, int weight);

        }

        void saveDialogCacheFood(DialogSavedCallback callback);

        interface DialogSavedCallback{
            void onDialogSaved();
        }

        void deleteDialogCacheFood();







    }
}
