package com.se_lab.se_proj.HealthMgmt.DailyProgress;

import com.se_lab.se_proj.BasePresenter;
import com.se_lab.se_proj.BaseView;

import java.util.Calendar;

public interface DailyProgressContract {

    interface View extends BaseView<Presenter> {

        void showDayOfWeek(int dayOfWeek);

    }

    interface Presenter extends BasePresenter {

        void getDate();

    }

    interface Model {

        void getDate(GetDateCallback callback);

        interface GetDateCallback {
            void onDateLoaded(Calendar calendar);
        }

    }
}