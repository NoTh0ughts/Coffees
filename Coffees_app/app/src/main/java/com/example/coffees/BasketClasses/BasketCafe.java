package com.example.coffees.BasketClasses;

import org.jetbrains.annotations.NotNull;

public class BasketCafe {
    public Integer cafeId;
    public String cafeName;

    public BasketCafe (Integer id, String name){
        this.cafeId = id;
        this.cafeName = name;
    }

    @Override
    public @NotNull String toString() {
        return cafeName;
    }
}
