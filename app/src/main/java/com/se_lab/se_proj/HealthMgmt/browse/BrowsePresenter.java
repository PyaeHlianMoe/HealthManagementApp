package com.se_lab.se_proj.HealthMgmt.browse;

import android.support.annotation.NonNull;

/**
 * Implementation of BrowseActivity presenter
 */

public class BrowsePresenter implements BrowseContract.Presenter {

    @NonNull
    private BrowseContract.Model mActivityModel;

    @NonNull
    private BrowseContract.View mActivityView;

    BrowsePresenter(@NonNull BrowseContract.Model model,
                    @NonNull BrowseContract.View view) {
        mActivityView = view;
        mActivityModel = model;

        mActivityView.setPresenter(this);
    }

    @Override
    public void start() {
        mActivityView.showMenu();
    }

    @Override
    public void onProductClicked() {
//        mActivityView.showProductDetails();
    }

    @Override
    public void onDashBoardClicked() {
        mActivityView.showDashboard();
    }

    @Override
    public void onHealthMgmtClicked() {
        mActivityView.showHealthMgmt();
    }

    @Override
    public void onHelpClicked() {
        mActivityView.showHelp();
    }

    @Override
    public void onProfileClicked() {
        mActivityView.showProfile();
    }

    @Override
    public void onNearbyClicked() {
        mActivityView.showNearby();
    }
}
