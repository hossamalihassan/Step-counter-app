package com.example.stepcounter.data;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.FragmentActivity;

public class SharedPreferencesHandler {

    SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyStepsCount";
    public SharedPreferencesHandler(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    }

    public void storeInt(String key, int value){
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public void storeFloat(String key, float value){
        sharedPreferences.edit().putFloat(key, value).apply();
    }

    public void storeString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public float getFloat(String key){
        return sharedPreferences.getFloat(key, 0);
    }
}
