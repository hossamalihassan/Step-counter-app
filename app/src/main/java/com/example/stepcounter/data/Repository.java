package com.example.stepcounter.data;

import android.content.Context;

public class Repository {
    private static Repository instance;
    private static SharedPreferencesHandler handler;

    private static int steps;
    private static float distance;
    private static String time;

    private Repository(Context context) {
        handler = new SharedPreferencesHandler(context);
    }

    public static Repository getInstance(Context context) {
        if(instance == null) {
            instance = new Repository(context);
            steps = handler.getSteps();
            distance = handler.getDistance();
            time = handler.getStopwatch();
        }
        return instance;
    }

    public static Repository getInstance() {
        return instance;
    }


    public static int getSteps() {
        return steps;
    }

    public static void setSteps(int steps) {
        Repository.steps = steps;
        handler.setSteps(steps);
    }

    public static float getDistance() {
        return distance;
    }

    public static void setDistance(float distance) {
        Repository.distance = distance;
        handler.setDistance(distance);
    }

    public static String getTime() {
        return time;
    }

    public static void setTime(String time) {
        Repository.time = time;
        handler.setStopwatch(time);
    }
}
