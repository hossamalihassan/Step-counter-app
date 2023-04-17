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
        return handler.getInt("steps");
    }

    public static void setSteps(int steps) {
        handler.storeInt("steps", steps);
    }

    public static float getDistance() {
        return handler.getFloat("distance");
    }

    public static void setDistance(float distance) {
        handler.storeFloat("distance", distance);
    }

    public static String getTime() {
        return handler.getString("stopwatch");
    }

    public static void setTime(String time) {
        handler.storeString("stopwatch", time);
    }

    public static int getGoal() { return handler.getInt("goal"); }

    public static void setGoal(int goal) { handler.storeInt("goal", goal); }

    public static String getUsername() {
        return handler.getString("username");
    }

    public static void setUsername(String username) {
        handler.storeString("username", username);
    }

    public static String getProfilePicPath() {
        return handler.getString("profilePicPath");
    }

    public static void setProfilePicPath(String profilePicPath) {
        handler.storeString("profilePicPath", profilePicPath);
    }
}
