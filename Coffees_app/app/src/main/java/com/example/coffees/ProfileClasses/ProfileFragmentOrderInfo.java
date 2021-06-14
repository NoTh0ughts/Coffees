package com.example.coffees.ProfileClasses;

import java.util.ArrayList;

public class ProfileFragmentOrderInfo {
    public Integer orderId;
    public ArrayList<ProfileFragmentOrderProduct> products;
    //public Integer total;
    public String address;
    public String dateOrder;

    public ProfileFragmentOrderInfo(Integer orderId, ArrayList<ProfileFragmentOrderProduct> products,
                                    //Integer total,
                                    String address, String dateOrder){
        this.orderId = orderId;
        this.products = products;
        //this.total = total;
        this.address = address;
        this.dateOrder = dateOrder;
    }
}
