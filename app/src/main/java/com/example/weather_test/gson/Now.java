package com.example.weather_test.gson;

import com.google.gson.annotations.SerializedName;

public class Now {
    @SerializedName("fl")
    public String fl;
    @SerializedName("tmp")
    public String temperature;
    @SerializedName("cond_code")
    public String cond_code;
    @SerializedName("cond_txt")
    public String cond_txt;
    @SerializedName("wind_dir")
    public String wind_dir;
    @SerializedName("wind_sc")
    public String wind_sc;
    @SerializedName("wind_spd")
    public String wind_spd;
    @SerializedName("hum")
    public String hum;
    @SerializedName("pcpn")
    public String pcpn;
    @SerializedName("pres")
    public String pres;
    @SerializedName("vis")
    public String vis;
    @SerializedName("cloud")
    public String cloud;
}
