package com.se_lab.se_proj.HealthMgmt.browse;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.se_lab.se_proj.BaseNavigation.BaseNavigationActivity;
import com.se_lab.se_proj.BaseNavigation.BaseNavigationActivityPresenter;
import com.se_lab.se_proj.HealthMgmt.Local.FoodDataSource;
import com.se_lab.se_proj.HealthMgmt.Local.FoodRepository;
import com.se_lab.se_proj.HealthMgmt.browse.menu.BrowseMenuFragment;
import com.se_lab.se_proj.HealthMgmt.reusablefragments.products.ProductsContract;
import com.se_lab.se_proj.HealthMgmt.reusablefragments.products.ProductsFragment;
import com.se_lab.se_proj.R;
import com.se_lab.se_proj.databinding.ActivityBrowseBinding;
import com.se_lab.se_proj.model.Product;

public class BrowseActivity extends BaseNavigationActivity implements BrowseContract.View,
        ProductsContract.View.ProductsClickedCallback {

    BrowseContract.Presenter mPresenter;
    BrowseModel mModel;
    FoodDataSource mFoodDataSource;

    @Override
    public void setPresenter(BrowseContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setupPresenter() {
        mFoodDataSource = FoodRepository.getInstance(getApplicationContext());
        mModel = BrowseModel.getInstance(mFoodDataSource);
        mPresenter = new BrowsePresenter(mModel,this);
        mPresenter.start();
    }

    @Override
    public BaseNavigationActivityPresenter getActivityPresenter() {
        return mPresenter;
    }

    public BrowseModel getActivityModel() {
        return mModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        ActivityBrowseBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_browse);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupPresenter();
    }

    @Override
    public void showMenu() {
        BrowseMenuFragment menuFragment = new BrowseMenuFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, menuFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void showProducts() {

        ProductsFragment foodFragment = new ProductsFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, foodFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void sendClickedProduct(Product product) {
        mPresenter.onProductClicked();
    }

}
