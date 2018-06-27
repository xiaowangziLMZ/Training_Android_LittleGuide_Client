package com.tsoiay.littleguide.util;

import android.widget.Toast;

import com.tsoiay.littleguide.WeatherActivity;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {


    public  static void sendOkHttpRequest(String address, okhttp3.Callback callback){

        OkHttpClient client = new OkHttpClient();
        Request request= new Request.Builder().url(address).build();
        client.newCall( request ).enqueue( callback );

    }
}
