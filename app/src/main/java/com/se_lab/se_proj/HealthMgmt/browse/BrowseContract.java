package com.se_lab.se_proj.HealthMgmt.browse;

import com.se_lab.se_proj.BaseNavigation.BaseNavigationActivityPresenter;
import com.se_lab.se_proj.BaseNavigation.BaseNavigationActivityView;

/**
 * Contract between BrowseActivity View, Presenter and Model
 */

public interface BrowseContract {

    interface View extends BaseNavigationActivityView<Presenter> {

        void showMenu();

        void showProducts();

    }

    interface Presenter extends BaseNavigationActivityPresenter {

        void onProductClicked();

    }

    interface Model {



    }
}