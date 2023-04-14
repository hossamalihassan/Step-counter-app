package com.example.stepcounter.helpers;

import static android.content.Context.ALARM_SERVICE;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.stepcounter.data.Repository;
import com.example.stepcounter.services.DailyReminderService;

import java.util.Calendar;

public class SettingsHandler {

    private TextView goalInput;
    private static int goal;
    private Activity activity;
    public SettingsHandler() {}

    public void updateGoal() {
        goal = Integer.parseInt(this.goalInput.getText().toString());
        if(goal < 999999999){
            Repository.setGoal(goal);
        } else {
            Toast.makeText(activity.getApplicationContext(), "Steps count can't exceed 999999999", Toast.LENGTH_SHORT).show();
        }
    }

    public void showDailyReminderTimeDialog(TextView reminderTimePicked) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String timePickedFormat = hourOfDay + ":" + minute;
                reminderTimePicked.setText(timePickedFormat);
                Repository.setDailyReminder(timePickedFormat);
                createNotification();
            }
        }, hour, minute, false);

        timePickerDialog.show();
    }

    public void createNotification() {
        String savedDailyReminder = Repository.getDailyReminder();
        String[] dailyReminder = savedDailyReminder.split(":");
        int hour = Integer.parseInt(dailyReminder[0]);
        int minutes = Integer.parseInt(dailyReminder[1]);
        Intent myIntent = new Intent(activity, DailyReminderService.class);
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent =PendingIntent.getActivity(activity,
                    0, new Intent(activity, getClass()).addFlags(
                            Intent.FLAG_ACTIVITY_SINGLE_TOP),
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(),
                    0, new Intent(activity.getApplicationContext(), getClass()).addFlags(
                            Intent.FLAG_ACTIVITY_SINGLE_TOP),
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.HOUR, hour);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60 * 24, pendingIntent);
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
