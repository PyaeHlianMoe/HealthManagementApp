package com.se_lab.se_proj.HealthMgmt.Local;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.se_lab.se_proj.HealthMgmt.Local.Database.FoodContract;
import com.se_lab.se_proj.R;
import com.se_lab.se_proj.model.Food;
import com.se_lab.se_proj.model.Nutrient;
import com.se_lab.se_proj.model.NutrientCategory;
import com.se_lab.se_proj.model.Product;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class FoodRepository implements FoodDataSource {

    private static final int PRODUCTS_WITH_QUERY_LOADER = 3;
    private static final int USED_FOOD_LOADER = 4;

    private static FoodRepository INSTANCE = null;

    private WeakReference<Context> mContext;

    private FoodRepository(@NonNull Context context) {
        mContext = new WeakReference<>(context);
    }

    public static FoodRepository getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new FoodRepository(context);
        }
        return INSTANCE;
    }

    /*
    Implementation of products methods
     */
    @Override
    public void getProducts(final GetProductsCallback callback, String nameQuery,
                            int productCategory, final SparseArray<NutrientCategory> nutrients) {

        int argsSize = 0;
        String andSeparator = " AND ";
        String selection = "";
        ArrayList<String> selectionArgs = new ArrayList<>();

        if(!nameQuery.isEmpty()) {
            selection += andSeparator + FoodContract.ProductsFTS.FTS_VIRTUAL_TABLE + " MATCH ? ";
            selectionArgs.add("^" + nameQuery.replace(' ', '*') + "*");
        }

        if (productCategory != Product.PRODUCT_ALL) {
            selection += andSeparator + FoodContract.ProductsEntry.COLUMN_PRODUCT_TYPE + " = ? ";
            selectionArgs.add(String.valueOf(productCategory));
            argsSize++;
        }

        if (nutrients != null) {
            int nutrientsSize = nutrients.size();
            // Iterate over each entry and if nutrient category is checked - add to selection
            for (int key = 0; key < nutrientsSize; key++) {

                String columnName = Nutrient.NUTRIENT_COLUMNS.get(key);

                NutrientCategory nutrient = nutrients.get(key);

                if (nutrient.isChecked()) {

                    if (nutrient.isMinimumValueChecked()) {
                        selection += andSeparator + columnName + " > ? ";
                        selectionArgs.add(String.valueOf(nutrient.getMinValue()));
                        argsSize++;
                    }
                    if (nutrient.isMaximumValueChecked()) {
                        selection += andSeparator + columnName + " < ? ";
                        selectionArgs.add(String.valueOf(nutrient.getMaxValue()));
                        argsSize++;
                    }
                }
            }
        }

        // Remove first "AND" from selection statement
        selection = selection.replaceFirst(andSeparator, "WHERE ");

        android.content.CursorLoader productsCursorLoader = new android.content.CursorLoader(mContext.get(),
                FoodContract.ProductsFTS.CONTENT_URI,
                null, // all columns
                selection, // selection based upon user's query, nutrients list and product category
                selectionArgs.toArray(new String[argsSize]),
                FoodContract.ProductsEntry.COLUMN_NAME // sort order by product name
        );

        productsCursorLoader.registerListener(PRODUCTS_WITH_QUERY_LOADER, new Loader.OnLoadCompleteListener<Cursor>() {
            @Override
            public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
                if (data == null || !data.moveToFirst()) {
                    callback.onDataNotAvailable();
                    return;
                }

                int columnsLength = data.getColumnCount();
                HashMap<String, Integer> columnsMap = new HashMap<>(columnsLength);

                for(int i = 0; i < columnsLength; i++){
                    columnsMap.put(data.getColumnName(i), i);
                }

                List<Product> mProductsList = new ArrayList<>();

                data.moveToFirst();
                do {
                    Product.ProductBuilder productBuilder = new Product.ProductBuilder();

                    String nameValue = data.getString(
                            columnsMap.get(FoodContract.ProductsEntry.COLUMN_NAME)
                    );
                    String[] nameParts = nameValue.split(";");
                    if (nameParts.length > 1) {
                        productBuilder
                                .name(nameParts[0])
                                .description(nameParts[1]);
                    } else {
                        productBuilder
                                .name(nameValue);
                    }
                    productBuilder
                            .id(data.getLong(
                                    columnsMap.get(FoodContract.ProductsEntry._ID)))
                            .type(data.getInt(
                                    columnsMap.get(FoodContract.ProductsEntry.COLUMN_PRODUCT_TYPE)))
                            .ndbnoIndex(data.getInt(
                                    columnsMap.get(FoodContract.ProductsEntry.COLUMN_NDBNO)));

                    // Number of nutrients columns in database
                    int nutrientsColumnsSize = Nutrient.NUTRIENT_COLUMNS.size();

                    for (int index = 0; index < nutrientsColumnsSize; index++) {
                        productBuilder.nutrient(
                                index,
                                data.getDouble(
                                        columnsMap.get(
                                                Nutrient.NUTRIENT_COLUMNS.get(index))
                                )
                        );
                    }
                    mProductsList.add(productBuilder.build());
                } while (data.moveToNext());
                callback.onProductsLoaded(mProductsList);
            }
        });
        productsCursorLoader.startLoading();

    }

    /*
    Implementation of nutrients methods
     */
    @Override
    public void getNutrients(GetNutrientsCallback callback) {
        String[] nutrientsNames = mContext.get().getResources().getStringArray(R.array.nutrients);
        int[] nutrientsIds = Nutrient.NUTRIENTS_IDS;
        int nutrientsSize = nutrientsIds.length;
        SparseArray<Nutrient> nutrients = new SparseArray<>(nutrientsSize);
        for (int i = 0; i < nutrientsSize; i++) {
            nutrients.put(nutrientsIds[i], new Nutrient(nutrientsNames[i]));
        }
        callback.onNutrientsLoaded(nutrients);
    }


    /*
    Implementation of used food methods
     */
    @Override
    public void getUsedFood(final GetUsedFoodCallback callback, long date) {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.setTimeInMillis(date);

        // Today at 0:00
        startDate.add(Calendar.DAY_OF_MONTH, -1);
        startDate.set(Calendar.HOUR, 12);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.SECOND, 0);
        startDate.set(Calendar.MILLISECOND, 0);

        // Today at 24:00
        endDate.setTimeInMillis(date);
        endDate.add(Calendar.DAY_OF_MONTH, 0);
        endDate.set(Calendar.HOUR, 12);
        endDate.set(Calendar.MINUTE, 0);
        endDate.set(Calendar.SECOND, 0);
        endDate.set(Calendar.MILLISECOND, 0);

        String selection =
                FoodContract.UsedFoodEntry.USED_FOOD_TABLE_NAME + "."
                        + FoodContract.UsedFoodEntry.COLUMN_DATE + " >= ? " + " AND "
                        + FoodContract.UsedFoodEntry.USED_FOOD_TABLE_NAME + "."
                        + FoodContract.UsedFoodEntry.COLUMN_DATE + " < ?";
        String[] selectionArgs = new String[]{
                String.valueOf(startDate.getTimeInMillis()),
                String.valueOf(endDate.getTimeInMillis())
        };

        final android.content.CursorLoader usedFoodCursorLoader = new android.content.CursorLoader(mContext.get(),
                FoodContract.UsedFoodEntry.CONTENT_URI,
                null, // all columns
                selection, // no selection statements
                selectionArgs, // no selection args
                FoodContract.UsedFoodEntry._ID // sort by ID
        );

        usedFoodCursorLoader.registerListener(USED_FOOD_LOADER, new Loader.OnLoadCompleteListener<Cursor>() {
            @Override
            public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
                List<Food> foods = FoodValues.getFoodFromCursor(data);
                if(foods == null) callback.onDataNotAvailable();
                else callback.onUsedFoodLoaded(foods);
                usedFoodCursorLoader.unregisterListener(this);
            }
        });

        usedFoodCursorLoader.startLoading();
    }

    @Override
    public void addToUsedFood(int mealId, Food food) {
        ContentValues foodValues = FoodValues.createUsedFoodValues(mealId, food);

        mContext.get().getContentResolver().insert(
                FoodContract.UsedFoodEntry.CONTENT_URI,
                foodValues
        );
    }

    @Override
    public void getRecentlyAddedFood(final FoodDataSource.GetRecentlyAddedFoodCallback callback) {

        final android.content.CursorLoader usedFoodCursorLoader = new android.content.CursorLoader(mContext.get(),
                FoodContract.UsedFoodEntry.CONTENT_URI,
                null, // all columns
                null, // no selection statements
                null, // no selection args
                FoodContract.UsedFoodEntry.COLUMN_DATE + " DESC" // sorting by date descending
        );

        usedFoodCursorLoader.registerListener(USED_FOOD_LOADER, new Loader.OnLoadCompleteListener<Cursor>() {
            @Override
            public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
                List<Food> foods = FoodValues.getFoodFromCursor(data);
                if(foods == null) callback.onDataNotAvailable();
                else callback.onFoodLoaded(foods);
                usedFoodCursorLoader.unregisterListener(this);
            }
        });

        usedFoodCursorLoader.startLoading();
    }
}
