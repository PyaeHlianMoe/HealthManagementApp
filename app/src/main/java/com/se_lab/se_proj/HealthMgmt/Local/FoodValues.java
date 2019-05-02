package com.se_lab.se_proj.HealthMgmt.Local;

import android.content.ContentValues;
import android.database.Cursor;

import com.se_lab.se_proj.HealthMgmt.Local.Database.FoodContract;
import com.se_lab.se_proj.model.Food;
import com.se_lab.se_proj.model.Nutrient;
import com.se_lab.se_proj.model.Product;

import java.util.ArrayList;
import java.util.List;

public class FoodValues {

    static ContentValues createUsedFoodValues(int mealId, Food food) {

        ContentValues values = new ContentValues();
        values.put(FoodContract.UsedFoodEntry.COLUMN_MEAL_ID, mealId);
        values.put(FoodContract.UsedFoodEntry.COLUMN_FOOD_TYPE, food.getFoodType());
        values.put(FoodContract.UsedFoodEntry.COLUMN_FOOD_ID, food.getId());
        values.put(FoodContract.UsedFoodEntry.COLUMN_WEIGHT, food.getWeight());
        values.put(FoodContract.UsedFoodEntry.COLUMN_DATE, food.getAddDate());

        return values;
    }

    static List<Food> getFoodFromCursor(Cursor data) {
        if (data == null) {
            return null;
        }
        ArrayList<Food> foods = new ArrayList<>();
        long previousDate = -1;
        Product productToAdd;

        if(!data.moveToFirst()){
            return foods;
        }

        do {
            int foodType = data.getInt(
                    data.getColumnIndex(FoodContract.UsedFoodEntry.COLUMN_FOOD_TYPE)
            );

            int mealId = data.getInt(
                    data.getColumnIndex(FoodContract.UsedFoodEntry.COLUMN_MEAL_ID)
            );

            switch (foodType) {
                case Food.FOOD_PRODUCT:
                    long productAddDate = data.getLong(
                            data.getColumnIndex(FoodContract.UsedFoodEntry.COLUMN_DATE)
                    );
                    productToAdd = getProductFromCursorRow(data, data.getPosition());
                    productToAdd.setAddDate(productAddDate);
                    productToAdd.setMealId(mealId);
                    foods.add(productToAdd);
                    break;
            }
        } while (data.moveToNext());

        return foods;
    }

    private static Product getProductFromCursorRow(Cursor data, int position) {
        data.moveToPosition(position);
        Product.ProductBuilder productBuilder = new Product.ProductBuilder();

        String nameValue = data.getString(
                data.getColumnIndex(FoodContract.ProductsEntry.COLUMN_NAME)
        );
        String[] nameParts = nameValue.split(";");
        if (nameParts.length > 1) {
            productBuilder
                    .name(nameParts[0])
                    .description(nameParts[1]);

        } else productBuilder.name(nameValue);

        productBuilder
                .id(data.getLong(
                        data.getColumnIndex(FoodContract.UsedFoodEntry._ID)))
                .type(data.getInt(
                        data.getColumnIndex(FoodContract.ProductsEntry.COLUMN_PRODUCT_TYPE)))
                .ndbnoIndex(data.getInt(
                        data.getColumnIndex(FoodContract.ProductsEntry.COLUMN_NDBNO)))
                .weight(data.getInt(
                        data.getColumnIndex(FoodContract.UsedFoodEntry.COLUMN_WEIGHT)));

        for (int index = 0; index < Nutrient.NUTRIENT_COLUMNS.size(); index++) {
            productBuilder.nutrient(
                    index,
                    data.getDouble(
                            data.getColumnIndex(
                                    Nutrient.NUTRIENT_COLUMNS.get(index)
                            )
                    )
            );
        }
        return productBuilder.build();
    }
}
