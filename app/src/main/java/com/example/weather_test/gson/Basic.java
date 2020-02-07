package com.example.weather_test.gson;

import com.google.gson.annotations.SerializedName;

public class Basic {
    @SerializedName("location")
    public String locationName;
    @SerializedName("parent_city")
    public String parent_cityName;
    @SerializedName("admin_area")
    public String admin_areaName;
}
