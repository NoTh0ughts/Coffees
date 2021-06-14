package com.example.coffees.MenuClasses;

import java.util.ArrayList;

public class ProductFragmentModel {
    public Integer id;
    public String name;
    public Integer calories;
    public ArrayList<NutritionInfo> nutritionInfo;
    public ArrayList<String> ingredients;

    public ProductFragmentModel(Integer id, String name, Integer calories,
                                ArrayList<NutritionInfo> nutritionInfo,
                                ArrayList<String> ingredients){
        this.id = id;
        this.name = name;
        this.calories = calories;
        this.nutritionInfo = nutritionInfo;
        this.ingredients = ingredients;
    }
}
