package com.tsoiay.littleguide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class newWeather extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_new_weather );
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences ( this );
        if (prefs.getString ( "weather", null ) != null) {
            Intent intent = new Intent ( this, WeatherActivity.class );
            startActivity ( intent );
            finish ();
        }
    }
}

