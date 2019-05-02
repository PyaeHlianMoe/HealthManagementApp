package com.se_lab.se_proj.BaseNavigation;

import com.se_lab.se_proj.BaseView;


/**
 * Interface which all activities with navigation drawer should implement
 */

public interface BaseNavigationActivityView<T> extends BaseView<T> {

    void showDashboard();

    void showHealthMgmt();

    void showHelp();

    void showProfile();

    void showNearby();

}
