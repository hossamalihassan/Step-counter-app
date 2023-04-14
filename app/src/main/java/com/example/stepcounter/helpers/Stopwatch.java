package com.example.stepcounter.helpers;

import android.os.SystemClock;
import android.util.Log;
import android.widget.Chronometer;

public class Stopwatch {

    private Chronometer chronometer;
    private long pauseOffset;
    private String savedStopwatch;
    public Stopwatch() {}

    public void startCounting() {
        if(savedStopwatch != null){
            String[] cArr = savedStopwatch.split(":");
            if (cArr.length == 2) {
                pauseOffset = (long) Integer.parseInt(cArr[0]) * 60 * 1000
                        + Integer.parseInt(cArr[1]) * 1000L;
            } else if (cArr.length == 3) {
                pauseOffset = (long) Integer.parseInt(cArr[0]) * 60 * 60 * 1000
                        + (long) Integer.parseInt(cArr[1]) * 60 * 1000
                        + Integer.parseInt(cArr[2]) * 1000L;
            }
        }

        chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
        savedStopwatch = "";
        chronometer.start();
    }

    public void stopCounting() {
        chronometer.stop();
        pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
    }

    public String getStopwatchCount() {
        return chronometer.getText().toString();
    }

    public void setChronometer(Chronometer chronometer) {
        this.chronometer = chronometer;
    }

    public void setSavedStopwatch(String savedStopwatch) {
        this.savedStopwatch = savedStopwatch;
    }
}
