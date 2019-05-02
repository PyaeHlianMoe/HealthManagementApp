package com.se_lab.se_proj.HealthMgmt.addfood;

import android.support.annotation.NonNull;
import android.util.Log;

import com.se_lab.se_proj.HealthMgmt.Local.FoodDataSource;
import com.se_lab.se_proj.HealthMgmt.Local.SharedPreferencesManager;
import com.se_lab.se_proj.HealthMgmt.Local.SharedPreferencesSource;
import com.se_lab.se_proj.HealthMgmt.addfood.menu.AddMenuContract;
import com.se_lab.se_proj.HealthMgmt.reusablefragments.products.ProductsContract;
import com.se_lab.se_proj.HealthMgmt.reusablefragments.adddialog.AddFoodDialogContract;
import com.se_lab.se_proj.model.Food;
import com.se_lab.se_proj.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class which controls data flow in AddActivity and performs computations to provide for
 * presenters in order to display to the user
 */

public class AddActivityModel implements
        AddActivityContract.Model,
        AddFoodDialogContract.Model,
        AddMenuContract.Model,
        ProductsContract.Model{

    private static AddActivityModel INSTANCE = null;

    private SharedPreferencesSource mSharedPreferencesManager;

    private FoodDataSource mFoodDataSource;

    // Id of meal to which user adds food
    private int mMealId;

    // State of view expansions in DishesFragment
    private ArrayList<Boolean> mDishesExpandedViewArray;

    // Dish deleted from UI but stored in order to enable user to restore it after
    // clicking "undo" button in SnackBar

    private int mRestoreDishCachePosition;

    private boolean mRestoreDishViewExpanded;

    // Clicked Food data to pass through fragments
    private Food mClickedFoodCache;

    private int mClickedFoodWeight;

    private boolean mClickedFoodEditMode;

    // Max weight of Food to set by user in SeekBar
    private int mMaxFoodWeight;

    // Query in SearchView in Products fragment
    private String mQuery;


    // Prevent direct instantiation
    private AddActivityModel(@NonNull FoodDataSource foodDataSource,
                             @NonNull SharedPreferencesSource manager) {
        mFoodDataSource = foodDataSource;
        mSharedPreferencesManager = manager;
    }

    /**
     * Returns single instance of the class
     *
     * @param foodDataSource - source of food data
     * @param manager        - source of data from SharedPreferences file
     */
    static AddActivityModel getInstance(@NonNull FoodDataSource foodDataSource,
                                        @NonNull SharedPreferencesManager manager) {
        if (INSTANCE == null) {
            INSTANCE = new AddActivityModel(foodDataSource, manager);
        }
        return INSTANCE;
    }

    /**
     * Retrieve max food weight from preferences
     */
    private void getPreferenceMaxWeight() {
        if (mMaxFoodWeight == 0)
            mMaxFoodWeight = mSharedPreferencesManager.getMaxFoodWeight();
    }

    /**
     * Save clicked object to pass through views, enable edit or adding food
     *
     * @param food - food to pass through views
     */
    @Override
    public void setDialogFoodCache(Food food) {
        mClickedFoodCache = food;
    }

    /**
     * Set mode of AddFoodDialog
     *
     * @param isInEditMode - true if dialog is displayed in edit mode
     */
    @Override
    public void setDialogMode(boolean isInEditMode) {
        mClickedFoodEditMode = isInEditMode;
    }

    /**
     * Set id of meal to which user adds food
     *
     * @param mealId - number of meal
     */
    @Override
    public void setMealId(int mealId) {
        mMealId = mealId;
    }

    /**
     * Retrieve clicked food
     *
     * @param callback - fired after getting data
     */
    @Override
    public void getDialogFoodCache(GetDialogFoodCallback callback) {
        getPreferenceMaxWeight();
        if (!mClickedFoodEditMode) {
            mClickedFoodWeight = mMaxFoodWeight / 10;
        }
        int progress = (mClickedFoodWeight * 100) / mMaxFoodWeight;
        callback.onFoodLoaded(mClickedFoodCache, progress, mClickedFoodEditMode);
    }

    /**
     * Delete clicked food from database
     */
    @Override
    public void deleteDialogCacheFood() {
        // No possibility to delete UsedFood from here
    }

    /**
     * Save clicked food in database
     */
    @Override
    public void saveDialogCacheFood(DialogSavedCallback callback) {
        mClickedFoodCache.setWeight(mClickedFoodWeight);
        mClickedFoodCache.setAddDate(System.currentTimeMillis());
        mFoodDataSource.addToUsedFood(mMealId, mClickedFoodCache);
        callback.onDialogSaved();
    }



    /**
     * Counts weight based on progress and max possible weight
     *
     * @param callback - called after values are counted
     * @param progress - current progress of SeekBar set in dialog
     */
    @Override
    public void getUpdatedWeightValuesPerProgress(GetUpdatedWeightValuesCallback callback, int progress) {
        getPreferenceMaxWeight();
        mClickedFoodWeight = (progress * mMaxFoodWeight) / 100;
        callback.onValuesUpdatedCounted(mClickedFoodCache, mClickedFoodWeight);
    }

    /*
    Implementation of methods from Products model
     */
    @Override
    public void getProducts(final GetProductsCallback callback) {
        FoodDataSource.GetProductsCallback productsCallback = new FoodDataSource.GetProductsCallback() {
            @Override
            public void onProductsLoaded(List<Product> productsList) {
                callback.onProductsLoaded(productsList);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        };
        mFoodDataSource.getProducts(
                productsCallback,
                mQuery,
                0,
                null);
    }

    @Override
    public void getQuery(GetQueryCallback callback) {
        callback.onQueryLoaded(mQuery);
    }

    @Override
    public void setQuery(String query) {
        mQuery = query;
    }

    @Override
    public void setChosenProduct(Product product) {
        mClickedFoodCache = product;
    }

    @Override
    public void getRecentlyAddedFood(final GetRecentlyAddedFoodCallback callback) {
        mFoodDataSource.getRecentlyAddedFood(new FoodDataSource.GetRecentlyAddedFoodCallback() {
            @Override
            public void onFoodLoaded(List<Food> foods) {
                callback.onFoodLoaded(foods);
            }

            @Override
            public void onDataNotAvailable() {
                Log.e("AddActivityModel", "Recently food data not available");
            }
        });
    }
}

