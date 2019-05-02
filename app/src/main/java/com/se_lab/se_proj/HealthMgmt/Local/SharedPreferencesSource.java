package com.se_lab.se_proj.HealthMgmt.Local;

import com.se_lab.se_proj.model.Diet;

public interface SharedPreferencesSource {
    boolean isFirstLaunch();

    int getMaxFoodWeight();

    void saveDiet(Diet diet);

    void getDiet(GetDietCallback callback);

    interface GetDietCallback {

        void onDietLoaded(Diet diet);

        void onDietNotSet();

    }
}
