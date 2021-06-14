package com.example.coffees.ProfileClasses;

public class UserOrdersFragmentModel {
    public Integer productId;
    public String productName;
    public Integer productCount;
    //public Integer price;

    public UserOrdersFragmentModel(Integer productId, String productName, Integer productCount
            //,Integer price
    ){
        this.productId = productId;
        this.productName = productName;
        this.productCount = productCount;
        //this.price = price;
    }
}
