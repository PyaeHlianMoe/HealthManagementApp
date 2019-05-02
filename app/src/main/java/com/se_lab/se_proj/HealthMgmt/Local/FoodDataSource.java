package com.se_lab.se_proj.HealthMgmt.Local;

import android.util.SparseArray;

import com.se_lab.se_proj.model.Food;
import com.se_lab.se_proj.model.Nutrient;
import com.se_lab.se_proj.model.NutrientCategory;
import com.se_lab.se_proj.model.Product;

import java.util.List;

public interface FoodDataSource {
    interface GetProductsCallback {

        void onProductsLoaded(List<Product> productsList);

        void onDataNotAvailable();
    }

    void getProducts(GetProductsCallback callback, String nameQuery, int productCategory,
                     SparseArray<NutrientCategory> nutrients);

    // Nutrients method
    interface GetNutrientsCallback {
        void onNutrientsLoaded(SparseArray<Nutrient> nutrients);
    }

    void getNutrients(GetNutrientsCallback callback);

    // Used Food methods
    interface GetUsedFoodCallback{

        void onUsedFoodLoaded(List<Food> foods);

        void onDataNotAvailable();

    }

    void getUsedFood(GetUsedFoodCallback callback, long date);

    interface GetRecentlyAddedFoodCallback {
        void onFoodLoaded(List<Food> foods);

        void onDataNotAvailable();
    }

    void getRecentlyAddedFood(GetRecentlyAddedFoodCallback callback);

    void addToUsedFood(int mealId, Food food);
}
