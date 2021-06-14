package com.example.hotelbooking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Booking {

    @SerializedName("user_room")
    @Expose
    private List<UserRoom> userRooms = new ArrayList<>();
    //todo aaa
    public List<UserRoom> getUserRooms() {
        return userRooms;
    }

    public void setUserRooms(List<UserRoom> userRooms) {
        this.userRooms = userRooms;
    }

}
