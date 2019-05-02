package com.se_lab.se_proj.HealthMgmt.browse;

import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.se_lab.se_proj.HealthMgmt.Local.FoodDataSource;
import com.se_lab.se_proj.HealthMgmt.browse.menu.BrowseMenuContract;
import com.se_lab.se_proj.HealthMgmt.reusablefragments.products.ProductsContract;
import com.se_lab.se_proj.model.Nutrient;
import com.se_lab.se_proj.model.NutrientCategory;
import com.se_lab.se_proj.model.Product;

import java.util.List;

/**
 * Model class which controls data flow in BrowseActivity, get data from FoodRepository
 * and store cached data
 */

public class BrowseModel implements
        BrowseContract.Model,
        BrowseMenuContract.Model,
        ProductsContract.Model {

    private static BrowseModel INSTANCE = null;

    private FoodDataSource mFoodDataSource;

    private SparseArray<NutrientCategory> mMenuNutrients = new SparseArray<>();

    private int mCategory;

    private String mQuery;

    private Product mClickedProduct;

    private BrowseModel(@NonNull FoodDataSource foodDataSource) {

        mFoodDataSource = foodDataSource;
        loadNutrientList();

    }

    static BrowseModel getInstance(@NonNull FoodDataSource foodDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new BrowseModel(foodDataSource);
        }
        return INSTANCE;
    }

    private void loadNutrientList() {
        mFoodDataSource.getNutrients(new FoodDataSource.GetNutrientsCallback() {
            @Override
            public void onNutrientsLoaded(SparseArray<Nutrient> nutrients) {

                // Convert Nutrient object to NutrientCategory by iterating of each entry in HashMap
                int nutrientsSize = nutrients.size();
                for (int index = 0; index < nutrientsSize; index++) {
                    mMenuNutrients.put(
                            index,
                            new NutrientCategory(nutrients.get(index)));
                }
            }
        });
    }

    @Override
    public void getNutrients(GetNutrientsCallback callback) {
        callback.onNutrientsLoaded(mMenuNutrients);
    }

    @Override
    public void setCategory(int category) {
        mCategory = category;
    }

    @Override
    public void setQuery(String query) {
        mQuery = query;
    }

    @Override
    public void setNutrientMinValue(int position, double value) {
        mMenuNutrients.get(position).setMinValue(value);
    }

    @Override
    public void setNutrientMinState(int position, boolean isChecked) {
        mMenuNutrients.get(position).setMinimumValueChecked(isChecked);
    }

    @Override
    public void setNutrientMaxValue(int position, double value) {
        mMenuNutrients.get(position).setMaxValue(value);
    }

    @Override
    public void setNutrientMaxState(int position, boolean isChecked) {
        mMenuNutrients.get(position).setMaximumValueChecked(isChecked);
    }

    @Override
    public void setNutrientState(int position, boolean isChecked) {
        mMenuNutrients.get(position).setChecked(isChecked);
    }

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
                mCategory,
                mMenuNutrients);
    }

    @Override
    public void getQuery(GetQueryCallback callback) {
        callback.onQueryLoaded(mQuery);
    }

    @Override
    public void setChosenProduct(Product product) {
        mClickedProduct = product;
    }

}
