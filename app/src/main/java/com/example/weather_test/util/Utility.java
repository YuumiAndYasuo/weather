package com.example.weather_test.util;

import android.util.Log;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.weather_test.R;
import com.example.weather_test.gson.Now;
import com.example.weather_test.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.Callback;
import okhttp3.OkHttpClient;

public class Utility {


    public static Weather handleWeatherResponse(String response){
        try{
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather6");
            String weatherContent=jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


//    public static Now handleNowResponse(String response){
//        try{
//            JSONObject jsonObject=new JSONObject(response);
//            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather6");
//            String weatherContent=jsonArray.getJSONObject(0).toString();
//            return new Gson().fromJson(weatherContent, Now.class);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return null;
//    }
}
