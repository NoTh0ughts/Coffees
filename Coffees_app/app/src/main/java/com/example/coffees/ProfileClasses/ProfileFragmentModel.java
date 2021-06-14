package com.example.coffees.ProfileClasses;

import java.util.ArrayList;

public class ProfileFragmentModel {
    public Integer id;
    public String username;
    public String since;
    public ArrayList<ProfileFragmentOrderInfo> orders;

    public ProfileFragmentModel(Integer id, String username, String since,
                                ArrayList<ProfileFragmentOrderInfo> orders){
        this.id = id;
        this.username = username;
        this.since = since;
        this.orders = orders;
    }
}
