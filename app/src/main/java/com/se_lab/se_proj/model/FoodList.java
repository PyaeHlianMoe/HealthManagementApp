package com.se_lab.se_proj.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class FoodList {

    private ArrayList<Food> branded;
    private ArrayList<Food> self;
    private ArrayList<Food> common;

    public FoodList(ArrayList<Food> branded, ArrayList<Food> self, ArrayList<Food> common) {
        this.branded = branded;
        this.self = self;
        this.common = common;
    }

    public ArrayList<Food> getBranded() {
        return branded;
    }

    public ArrayList<Food> getSelf() {
        return self;
    }

    public ArrayList<Food> getCommon() {
        return common;
    }
}
