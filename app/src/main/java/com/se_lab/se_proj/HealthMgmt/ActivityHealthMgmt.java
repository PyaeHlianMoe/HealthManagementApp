package com.se_lab.se_proj.HealthMgmt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.se_lab.se_proj.BaseNavigation.BaseNavigationActivity;
import com.se_lab.se_proj.BaseNavigation.BaseNavigationActivityPresenter;
import com.se_lab.se_proj.HealthMgmt.DailyProgress.DailyProgressFragment;
import com.se_lab.se_proj.HealthMgmt.Local.FoodDataSource;
import com.se_lab.se_proj.HealthMgmt.Local.FoodRepository;
import com.se_lab.se_proj.HealthMgmt.Local.ProductsDataManager;
import com.se_lab.se_proj.HealthMgmt.Local.ProductsDataSource;
import com.se_lab.se_proj.HealthMgmt.Local.SharedPreferencesManager;
import com.se_lab.se_proj.HealthMgmt.addfood.AddActivity;
import com.se_lab.se_proj.HealthMgmt.diet.DietActivity;
import com.se_lab.se_proj.R;
import com.se_lab.se_proj.model.Diet;

import java.util.ArrayList;

public class ActivityHealthMgmt extends BaseNavigationActivity implements HealthContract.View{

    public static final String MEAL_NUMBER = "meal_number";

    public static final String FRAGMENT_DAILY_MEALS_TAG = "daily_meals";

    HealthContract.Presenter mPresenter;

    HealthModel mModel;

    ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    public void setPresenter(HealthContract.Presenter presenter){
        mPresenter = presenter;
    }

    @Override
    public void setupPresenter() {
        FoodDataSource mFoodDataSource = FoodRepository.getInstance(getApplicationContext());

        SharedPreferencesManager mSharedPreferencesManager = SharedPreferencesManager.getInstance(
                getSharedPreferences(
                        getString(R.string.sh_pref_user_preferences_file_key),
                        MODE_PRIVATE)
        );

        ProductsDataSource mProductsDataManager = ProductsDataManager.getInstance(
                getApplicationContext());

        mModel = HealthModel.getInstance(
                mFoodDataSource,
                mSharedPreferencesManager,
                mProductsDataManager
        );

        mPresenter = new HealthPresenter(mModel,this);
    }

    @Override
    public BaseNavigationActivityPresenter getActivityPresenter() {
        return mPresenter;
    }

    public HealthModel getActivityModel(){ return mModel; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupPresenter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        for(Fragment fragment : mFragments){
            getSupportFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void showNewDiet() {
        Intent launchNewDietIntent = new Intent(this, DietActivity.class);
        startActivity(launchNewDietIntent);
    }

    @Override
    public void showAddFood(int mealNumber) {
        Intent addActivityIntent = new Intent(this, AddActivity.class);
        addActivityIntent.putExtra(AddActivity.MEAL_ID_KEY, mealNumber);
        startActivity(addActivityIntent);
    }

    @Override
    public void showDailyMeals(Diet diet) {
        Fragment mDailyProgressFragment = new DailyProgressFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mDailyProgressFragment, FRAGMENT_DAILY_MEALS_TAG);
        fragmentTransaction.commit();
        fragmentTransaction.addToBackStack(null);
        mFragments.add(mDailyProgressFragment);
    }
}
