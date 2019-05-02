package com.se_lab.se_proj.HealthMgmt;

import com.se_lab.se_proj.BaseNavigation.BaseNavigationActivityPresenter;
import com.se_lab.se_proj.BaseNavigation.BaseNavigationActivityView;
import com.se_lab.se_proj.model.Diet;

public class HealthContract {

    public interface View extends BaseNavigationActivityView<Presenter> {

        void showNewDiet();

        void showAddFood(int mealNumber);

        void showDailyMeals(Diet diet);

    }

    public interface Presenter extends BaseNavigationActivityPresenter {

        void getDiet();

    }

    public interface Model{

        void checkDatabaseState();

        void getDiet(GetDietCallback callback);

        interface GetDietCallback {

            void onDietLoaded(Diet diet);

            void onDietNotSet();

        }
    }
}
