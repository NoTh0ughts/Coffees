package com.example.coffees.GlobalClasses;

import java.util.ArrayList;

public class UserInfo {
    public static Integer id = null;
    public static String username = null;
    public static String since = null;
    public static Integer cardId = null;
    public static Integer cardDiscount = null;
    public static Integer countInBasket = null;
    public static ArrayList<Integer> favorites = new ArrayList<>();

//    public UserInfo(Integer id, String username, String since, Integer cardId,
//                    //Integer cardDiscount,
//                    Integer countInBasket, ArrayList<Integer> favorites){
//        UserInfo.id = id;
//        UserInfo.username = username;
//        UserInfo.since = since;
//        UserInfo.cardId = cardId;
//        //UserInfo.cardDiscount = cardDiscount;
//        UserInfo.countInBasket = countInBasket;
//        UserInfo.favorites = favorites;
//    }

    public static void logOut(){
        id = null;
        username = null;
        since = null;
        cardId = null;
        cardDiscount = null;
        countInBasket = null;
        favorites = new ArrayList<>();
    }
}
