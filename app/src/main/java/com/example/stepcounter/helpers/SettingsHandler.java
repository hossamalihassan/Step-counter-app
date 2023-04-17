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

import com.example.stepcounter.alarmReceiver.DailyReminderReceiver;
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

//    public void showDailyReminderTimeDialog(TextView reminderTimePicked) {
//        Calendar calendar = Calendar.getInstance();
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        int minute = calendar.get(Calendar.MINUTE);
//
//        TimePickerDialog timePickerDialog = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
//
//            @Override
//            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                String timePickedFormat = hourOfDay + ":" + minute;
//                reminderTimePicked.setText(timePickedFormat);
//                setDailyReminderAlarm();
//            }
//        }, hour, minute, false);
//
//        timePickerDialog.show();
//    }
//
//    public void setDailyReminderAlarm() {
//        String savedDailyReminder = Repository.getDailyReminder();
//        String[] dailyReminder = savedDailyReminder.split(":");
//        int hour = Integer.parseInt(dailyReminder[0]);
//        int minutes = Integer.parseInt(dailyReminder[1]);
//
//        Intent myIntent = new Intent(activity.getApplicationContext(), DailyReminderReceiver.class);
//        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(ALARM_SERVICE);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity.getApplicationContext(), 0, myIntent, PendingIntent.FLAG_IMMUTABLE);
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MINUTE, minutes);
//        calendar.set(Calendar.HOUR_OF_DAY, hour);
//        alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            CharSequence name = "dailyReminderChannel";
            String description = "A daily reminder";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel("dailyReminderID", name, importance);
            notificationChannel.setDescription(description);

            NotificationManager notificationManager = activity.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public void resetCounter() {
        Repository.setTime("00:00");
        Repository.setDistance(0);
        Repository.setSteps(0);
        Counter.resetCounter();
    }

//    public void createNotification() {
//        String savedDailyReminder = Repository.getDailyReminder();
//        String[] dailyReminder = savedDailyReminder.split(":");
//        int hour = Integer.parseInt(dailyReminder[0]);
//        int minutes = Integer.parseInt(dailyReminder[1]);
//        Intent myIntent = new Intent(activity, DailyReminderService.class);
//        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(ALARM_SERVICE);
//        PendingIntent pendingIntent;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            pendingIntent =PendingIntent.getActivity(activity,
//                    0, new Intent(activity, getClass()).addFlags(
//                            Intent.FLAG_ACTIVITY_SINGLE_TOP),
//                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
//        } else {
//            pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(),
//                    0, new Intent(activity.getApplicationContext(), getClass()).addFlags(
//                            Intent.FLAG_ACTIVITY_SINGLE_TOP),
//                    PendingIntent.FLAG_UPDATE_CURRENT);
//        }
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MINUTE, minutes);
//        calendar.set(Calendar.HOUR, hour);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60 * 24, pendingIntent);
//    }

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
