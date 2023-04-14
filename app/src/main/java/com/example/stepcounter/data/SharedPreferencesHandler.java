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

    public void setSteps(int value){
        sharedPreferences.edit().putInt("steps", value).apply();
    }

    public void setDistance(float value){
        sharedPreferences.edit().putFloat("distance", value).apply();
    }

    public void setStopwatch(String value) {
        sharedPreferences.edit().putString("stopwatch", value).apply();
    }

    public void setGoal(int goal) {
        sharedPreferences.edit().putInt("goal", goal).apply();
    }

    public void setDailyReminder(String dailyReminder) {
        sharedPreferences.edit().putString("dailyReminder", dailyReminder).apply();
    }

    public int getSteps(){
        return sharedPreferences.getInt("steps", 0);
    }

    public float getDistance(){
        return sharedPreferences.getFloat("distance", 0);
    }

    public String getStopwatch() { return sharedPreferences.getString("stopwatch", ""); }

    public int getGoal() { return sharedPreferences.getInt("goal", 0); }

    public String getDailyReminder() { return sharedPreferences.getString("dailyReminder", ""); }
}
