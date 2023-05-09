package com.example.stepcounter.helpers;

import static android.content.Context.ALARM_SERVICE;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.stepcounter.data.Repository;

import java.util.Calendar;

public class SettingsHandler {

    private TextView goalInput;
    private static int goal;
    private Activity activity;
    public SettingsHandler() {}

    public void updateGoal() {
        String goalInputText = this.goalInput.getText().toString();
        if(goalInputText.equals("")) {
            Toast.makeText(activity.getApplicationContext(), "Steps count can't be null", Toast.LENGTH_SHORT).show();
            return;
        }

        goal = Integer.parseInt(this.goalInput.getText().toString());
        if(goal < 999999999){
            Repository.setGoal(goal);
        } else {
            Toast.makeText(activity.getApplicationContext(), "Steps count can't exceed 999999999", Toast.LENGTH_SHORT).show();
        }
    }

    public void resetCounter() {
        Repository.setTime("00:00");
        Repository.setDistance(0);
        Repository.setSteps(0);
        Counter.resetCounter();
        DatabaseHelper.updateLog(0, 0, false);
    }

    public void setGoalInput(TextView goalInput) {
        this.goalInput = goalInput;
        this.goalInput.setText(String.valueOf(Repository.getGoal()));
    }

    public static int getGoal() {
        return goal;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
