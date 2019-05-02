package com.se_lab.se_proj.HealthMgmt.diet.summary;

import com.se_lab.se_proj.BasePresenter;
import com.se_lab.se_proj.BaseView;
import com.se_lab.se_proj.model.Diet;

/**
 * Contract class between SummaryFragment View and Presenter
 */

public interface SummaryContract {

    interface View extends BaseView<Presenter> {

        void updateView(Diet diet);

        void showPreviousFragment();

        void saveDiet();

    }

    interface Presenter extends BasePresenter {

        void getStartValues();

        void onBackClicked();

        void onSaveClicked();

    }

    interface Model {

        void getDietSummary(GetDietSummaryCallback callback);

        interface GetDietSummaryCallback {

            void onSummaryLoaded(Diet diet);

        }

    }

}
