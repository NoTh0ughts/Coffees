package com.example.coffees.BasketClasses;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public class BasketCity {
    public Integer cityId;
    public String cityName;

    public BasketCity (Integer id, String name){
        this.cityId = id;
        this.cityName = name;
    }

    @Override
    public @NotNull String toString() {
        return cityName;
    }
}
