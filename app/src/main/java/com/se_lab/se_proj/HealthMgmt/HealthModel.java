package com.se_lab.se_proj.HealthMgmt;

import android.support.annotation.NonNull;
import android.util.Log;

import com.se_lab.se_proj.HealthMgmt.DailyProgress.DailyProgressContract;
import com.se_lab.se_proj.HealthMgmt.DailyProgressPage.DailyProgressPageContract;
import com.se_lab.se_proj.HealthMgmt.Local.FoodDataSource;
import com.se_lab.se_proj.HealthMgmt.Local.ProductsDataSource;
import com.se_lab.se_proj.HealthMgmt.Local.SharedPreferencesSource;
import com.se_lab.se_proj.model.Diet;
import com.se_lab.se_proj.model.DietProgress;
import com.se_lab.se_proj.model.Food;
import com.se_lab.se_proj.model.Meal;

import java.util.Calendar;
import java.util.List;

public class HealthModel implements
        HealthContract.Model,
        DailyProgressContract.Model,
        DailyProgressPageContract.Model {

    private static HealthModel INSTANCE = null;

    private FoodDataSource mFoodDataSource;

    private ProductsDataSource mProductsDataManager;

    private SharedPreferencesSource mSharedPreferencesManager;

    private Diet mDiet;

    private Calendar mDisplayedDate;

    private HealthModel(@NonNull FoodDataSource foodDataSource,
                        @NonNull SharedPreferencesSource manager,
                        @NonNull ProductsDataSource productsDataSource){
        mFoodDataSource = foodDataSource;
        mSharedPreferencesManager = manager;
        mProductsDataManager = productsDataSource;

        checkDatabaseState();
        mDisplayedDate = Calendar.getInstance();
        mDisplayedDate.setTimeInMillis(System.currentTimeMillis());
    }

    static HealthModel getInstance(@NonNull FoodDataSource foodDataSource,
                                 @NonNull SharedPreferencesSource manager,
                                 @NonNull ProductsDataSource productsDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new HealthModel(foodDataSource, manager, productsDataSource);
        }
        return INSTANCE;
    }

    /**
     * If it is first launch, products table needs to be populated from products file
     */
    @Override
    public void checkDatabaseState() {
        if(mSharedPreferencesManager.isFirstLaunch()) {
            mProductsDataManager.insertProductsDataToDatabase();
        }
    }

    /**
     * Used to get diet from SharedPreferencesSource and inform activity if user had set
     * his/her diet plan.
     *
     * @param callback - called after getting diet plan from SharedPreferences
     */
    @Override
    public void getDiet(final HealthContract.Model.GetDietCallback callback) {
        mSharedPreferencesManager.getDiet(new SharedPreferencesSource.GetDietCallback() {
            @Override
            public void onDietLoaded(@NonNull Diet diet) {
                mDiet = diet;
                callback.onDietLoaded(diet);
            }

            @Override
            public void onDietNotSet() {
                callback.onDietNotSet();
            }
        });
    }

    /**
     * Used in DailyProgressFragment to get information about diet.
     *
     * @param callback - used to send back diet plan
     */
    @Override
    public void getDietProgress(final GetDietProgressCallback callback, long date) {
        FoodDataSource.GetUsedFoodCallback usedFoodCallback = new FoodDataSource.GetUsedFoodCallback() {
            @Override
            public void onUsedFoodLoaded(List<Food> foods) {
                DietProgress dailyDietProgress = new DietProgress(mDiet);
                for(Food food : foods){
                    dailyDietProgress.addFoodToMeal(food.getMealId(), food);
                }
                callback.onDietProgressLoaded(dailyDietProgress);
            }

            @Override
            public void onDataNotAvailable() {
                Log.e("Error", "Daily meals data is not available");
            }
        };
        mFoodDataSource.getUsedFood(usedFoodCallback, date);
    }

    @Override
    public void getDate(GetDateCallback callback) {
        callback.onDateLoaded(mDisplayedDate);
    }
}
