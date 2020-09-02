package com.example.weatherapp;

import android.app.Activity;
import android.content.SharedPreferences;

public class CityPreference {

    SharedPreferences preferences;

    public CityPreference(Activity activity) {
        preferences = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    public String getCity() {
        return preferences.getString("city", "Sydney, AU");
    }

    public void setCity(String city) {
        preferences.edit().putString("city", city).apply();
    }
}
