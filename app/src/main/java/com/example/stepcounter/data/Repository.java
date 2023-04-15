package com.example.stepcounter.data;

import android.content.Context;

public class Repository {
    private static Repository instance;
    private static SharedPreferencesHandler handler;

    private Repository(Context context) {
        handler = new SharedPreferencesHandler(context);
    }

    public static Repository getInstance(Context context) {
        if(instance == null) {
            instance = new Repository(context);
        }
        return instance;
    }

    public static Repository getInstance() {
        return instance;
    }


    public static int getSteps() {
        return handler.getSteps();
    }

    public static void setSteps(int steps) {
        handler.setSteps(steps);
    }

    public static float getDistance() {
        return handler.getDistance();
    }

    public static void setDistance(float distance) {
        handler.setDistance(distance);
    }

    public static String getTime() {
        return handler.getStopwatch();
    }

    public static void setTime(String time) {
        handler.setStopwatch(time);
    }

    public static int getGoal() { return handler.getGoal(); }

    public static void setGoal(int goal) { handler.setGoal(goal); }

    public static String getDailyReminder() {
        return handler.getDailyReminder();
    }

    public static void setDailyReminder(String dailyReminder){
        handler.setDailyReminder(dailyReminder);
    }

    public static String getUsername() {
        return handler.getUsername();
    }

    public static void setUsername(String username) {
        handler.setUsername(username);
    }

    public static String getProfilePicPath() {
        return handler.getProfilePicPath();
    }

    public static void setProfilePicPath(String profilePicPath) {
        handler.setProfilePicPath(profilePicPath);
    }
}
