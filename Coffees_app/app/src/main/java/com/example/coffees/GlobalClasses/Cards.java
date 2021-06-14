package com.example.coffees.GlobalClasses;

import com.example.coffees.R;

import java.util.HashMap;

public class Cards {
    public static Integer defaultCard = R.drawable.discountcard_default;

    public static final HashMap<Integer, Integer> cardsPictures = new HashMap<>();
    static {
        cardsPictures.put(1, R.drawable.discountcard_5);
        cardsPictures.put(2, R.drawable.discountcard_10);
        cardsPictures.put(3, R.drawable.discountcard_15);

    }
}
