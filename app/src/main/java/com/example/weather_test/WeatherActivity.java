package com.example.weather_test;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.example.weather_test.gson.Forecast;
import com.example.weather_test.gson.LifeStyle;
import com.example.weather_test.gson.Now;
import com.example.weather_test.gson.Weather;
import com.example.weather_test.util.HttpUtil;
import com.example.weather_test.util.Utility;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    private TextView locationText;
    private TextView updatetimeText;
    private LinearLayout suggestionLayout;
    private TextView flText;
    private TextView tmpText;
    private ImageView condImg;
    private TextView condText;
    private TextView winddirText;
    private TextView windscText;
    private TextView windspdText;
    private TextView humText;
    private TextView pcpnText;
    private TextView presText;
    private TextView visText;
    private TextView cloudText;
    private LinearLayout forecastLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        locationText=(TextView)findViewById(R.id.location_text);
        updatetimeText=(TextView)findViewById(R.id.updatetime_text);
        suggestionLayout=(LinearLayout)findViewById(R.id.suggestion_layout);
        flText=(TextView)findViewById(R.id.fl_text);
        tmpText=(TextView)findViewById(R.id.tmp_text);
        condImg=(ImageView) findViewById(R.id.cond_img);
        condText=(TextView)findViewById(R.id.cond_text);
        winddirText=(TextView)findViewById(R.id.winddir_text);
        windscText=(TextView)findViewById(R.id.windsc_text);
        windspdText=(TextView)findViewById(R.id.windspd_text);
        humText=(TextView)findViewById(R.id.hum_text);
        pcpnText=(TextView)findViewById(R.id.pcpn_text);
        presText=(TextView)findViewById(R.id.pres_text);
        visText=(TextView)findViewById(R.id.vis_text);
        cloudText=(TextView)findViewById(R.id.cloud_text);
        forecastLayout=(LinearLayout)findViewById(R.id.forecast_layout);

        
        //定位获得经纬度
        AMapLocationClient aMapLocationClient;
        TextView location;
        aMapLocationClient=new AMapLocationClient(getApplicationContext());

        AMapLocationListener aMapLocationListener=new AMapLocationListener() {
            String lastlocation="";
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
//可在其中解析amapLocation获取相应内容。
                        Log.d("ffff", "上次地址"+lastlocation);
                        if(lastlocation.equals(aMapLocation.getAddress())){
                            return;
                        }else {
                            lastlocation=aMapLocation.getAddress();
                        }
                        Log.d("fffff", "onLocationChanged: "+aMapLocation.getLatitude()+aMapLocation.getAddress());
                        requestWeather(aMapLocation.getLongitude()+"",aMapLocation.getLatitude()+"");
                    }else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError","location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }
        };
        aMapLocationClient.setLocationListener(aMapLocationListener);
        AMapLocationClientOption aMapLocationClientOption=new AMapLocationClientOption();
        aMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        aMapLocationClientOption.setOnceLocationLatest(true);
        //aMapLocationClientOption.setInterval(3000);
        aMapLocationClient.setLocationOption(aMapLocationClientOption);
        aMapLocationClient.startLocation();
        //requestWeather("minquan");

    }

    public void requestWeather(final String jingdu,final String weidu){
        String key="db97563d3b9142f494fc637b73acc7b7";
        String lifestyle_url="https://free-api.heweather.net/s6/weather/lifestyle?location="+jingdu+","+weidu+"&key="+key;
        String now_url="https://free-api.heweather.net/s6/weather/now?location="+jingdu+","+weidu+"&key="+key;
        String forecast_url="https://free-api.heweather.net/s6/weather/forecast?location="+jingdu+","+weidu+"&key="+key;
        //生活指数
        HttpUtil.sendHttpOk3Request(lifestyle_url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                final String responseText=response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(WeatherActivity.this,responseText,Toast.LENGTH_SHORT).show();
                        Weather weather=Utility.handleWeatherResponse(responseText);
                        if(weather.status.equals("ok")){
                            locationText.setText(weather.basic.admin_areaName+weather.basic.parent_cityName+weather.basic.locationName);
                            updatetimeText.setText(weather.update.loctime);
                            suggestionLayout.removeAllViews();
                            for(LifeStyle lifeStyle:weather.lifeStyleList){
                                View view= LayoutInflater.from(WeatherActivity.this).inflate(R.layout.suggestion_item,suggestionLayout,false);
                                TextView type=(TextView)view.findViewById(R.id.type_text);
                                TextView brf=(TextView)view.findViewById(R.id.brf_text);
                                TextView suggestioninfo=(TextView)view.findViewById(R.id.suggestioninfo_text);
                                switch (lifeStyle.type){
                                    case "comf":
                                        type.setText("舒适度指数");
                                        break;
                                    case "cw":
                                        type.setText("洗车指数");
                                        break;
                                    case "drsg":
                                        type.setText("穿衣指数");
                                        break;
                                    case "flu":
                                        type.setText("感冒指数");
                                        break;
                                    case "sport":
                                        type.setText("运动指数");
                                        break;
                                    case "trav":
                                        type.setText("旅游指数");
                                        break;
                                    case "uv":
                                        type.setText("紫外线指数");
                                        break;
                                    case "air":
                                        type.setText("空气污染扩散条件指数");
                                        break;
                                    case "ac":
                                        type.setText("空调开启指数");
                                        break;
                                    case "ag":
                                        type.setText("过敏指数");
                                        break;
                                    case "gl":
                                        type.setText("太阳镜指数");
                                        break;
                                    case "mu":
                                        type.setText("化妆指数");
                                        break;
                                    case "airc":
                                        type.setText("晾晒指数");
                                        break;
                                    case "ptfc":
                                        type.setText("交通指数");
                                        break;
                                    case "fsh":
                                        type.setText("钓鱼指数");
                                        break;
                                    case "spi":
                                        type.setText("防晒指数");
                                        break;
                                    default:
                                        type.setText("未知指数");
                                }

                                brf.setText(lifeStyle.brf);
                                suggestioninfo.setText(lifeStyle.suggestion+"\n");
                                suggestionLayout.addView(view);
                            }
                        }

                    }
                });
            }
        });
        //当前天气
        HttpUtil.sendHttpOk3Request(now_url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                final String responseText=response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(WeatherActivity.this,responseText,Toast.LENGTH_SHORT).show();
                        Weather weather=Utility.handleWeatherResponse(responseText);
                        if(weather.status.equals("ok")){
                            flText.setText(weather.now.fl+"℃");
                            tmpText.setText(weather.now.temperature+"℃");
                            Glide.with(WeatherActivity.this).load("https://cdn.heweather.com/cond_icon/"+weather.now.cond_code+".png").into(condImg);
                            condText.setText(weather.now.cond_txt);
                            winddirText.setText(weather.now.wind_dir);
                            windscText.setText(weather.now.wind_sc+"级");
                            windspdText.setText(weather.now.wind_spd+"千米/小时");
                            humText.setText(weather.now.hum);
                            pcpnText.setText(weather.now.pcpn);
                            presText.setText(weather.now.pres);
                            visText.setText(weather.now.vis+"公里");
                            cloudText.setText(weather.now.cloud);
                        }

                    }
                });
            }
        });
        //今天明天后天三天预报
        HttpUtil.sendHttpOk3Request(forecast_url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                final String responseText=response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(WeatherActivity.this,responseText,Toast.LENGTH_SHORT).show();
                        Weather weather=Utility.handleWeatherResponse(responseText);
                        if(weather.status.equals("ok")){
                            forecastLayout.removeAllViews();
                            for(Forecast forecast:weather.forecastList){
                                View view=LayoutInflater.from(WeatherActivity.this).inflate(R.layout.forecast_item,forecastLayout,false);
                                TextView dateText=(TextView)view.findViewById(R.id.date_text);
                                ImageView cond_code_dImg=(ImageView)view.findViewById(R.id.condcode_d_img);
                                TextView cond_code_dText=(TextView)view.findViewById(R.id.condcode_d_text);
                                ImageView cond_code_nImg=(ImageView)view.findViewById(R.id.condcode_n_img);
                                //TextView cond_code_nText=(TextView)view.findViewById(R.id.condcode_n_text);
                                TextView tmp_maxText=(TextView)view.findViewById(R.id.tmp_max_text);
                                TextView tmp_minText=(TextView)view.findViewById(R.id.tmp_min_text);

                                dateText.setText(forecast.date);
                                Glide.with(WeatherActivity.this).load("https://cdn.heweather.com/cond_icon/"+forecast.cond_code_d+".png").into(cond_code_dImg);
                                cond_code_dText.setText(forecast.cond_txt_d+"转"+forecast.cond_txt_n);
                                //cond_code_nText.setText(forecast.cond_txt_n);
                                Glide.with(WeatherActivity.this).load("https://cdn.heweather.com/cond_icon/"+forecast.cond_code_n+".png").into(cond_code_nImg);

                                tmp_maxText.setText(forecast.tmp_max+"℃");
                                tmp_minText.setText(forecast.tmp_min+"℃");
                                forecastLayout.addView(view);
                            }
                        }

                    }
                });
            }
        });
    }


}
