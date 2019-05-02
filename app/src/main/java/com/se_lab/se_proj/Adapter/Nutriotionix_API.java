package com.se_lab.se_proj.Adapter;

import com.se_lab.se_proj.model.Food;
import com.se_lab.se_proj.model.FoodList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface Nutriotionix_API {

    String BASE_URL = "https://trackapi.nutritionix.com";

    @Headers({
            "x-app-id:a01a04f9",
            "x-app-key:679457dc6a683d085bd91be58c79fbc0"
    })

    @GET("/v2/search/instant")
    Call<FoodList> getFood(@Query("query") String food);
}
