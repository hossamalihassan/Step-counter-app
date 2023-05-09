package com.example.stepcounter.helpers;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.stepcounter.data.Repository;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

public class ProfileHandler {

    ArrayList<String> log_date;
    ArrayList<Integer> log_steps_count;
    ArrayList<Float> log_distance;
    ArrayList<Integer> log_achieved_goal;
    TextView usernameTextView, totalStepsTextView, totalDistanceTextView;
    ImageView profilePictureView;
    LogsListAdapter logsView;

    public ProfileHandler() {

    }

    public void displayUserData() {
        getLogsFromDB();

        displayUsername();

        displayProfilePicture();

        displayStatistics();

        displayLastFiveDaysLogs();
    }

    public void displayProfilePicture() {
        String profilePicturePath = "/Pictures/StepCounter/" + Repository.getProfilePicPath();
        String filePath = Environment.getExternalStorageDirectory() + profilePicturePath;
        File file = new File(filePath);
        Uri uri = Uri.fromFile(file);
        profilePictureView.setImageURI(uri);
    }

    public void displayUsername() {
        usernameTextView.setText(Repository.getUsername());
    }

    public void getLogsFromDB() {
        log_date = new ArrayList<>();
        log_steps_count = new ArrayList<>();
        log_distance = new ArrayList<>();
        log_achieved_goal = new ArrayList<>();

        Cursor cursor = DatabaseHelper.getLastFiveDaysLogs();
        if(cursor.getCount() == 0){
            Log.d("data", "no data");
        } else {
            while(cursor.moveToNext()){
                log_date.add(cursor.getString(0));
                log_steps_count.add(cursor.getInt(1));
                log_distance.add(cursor.getFloat(2));
                log_achieved_goal.add(cursor.getInt(3));
            }
        }
    }

    public void displayLastFiveDaysLogs() {
        logsView.setLog_steps(log_steps_count);
        logsView.setLog_date(log_date);
    }

    public void displayStatistics() {
        int totalSteps = 0;
        Cursor totalStepsCursor = DatabaseHelper.getTotalStepsCount();
        while(totalStepsCursor.moveToNext()){
            totalSteps = totalStepsCursor.getInt(0);
        }

        float totalDistance = 0;
        Cursor totalDistanceCursor = DatabaseHelper.getTotalDistance();
        while(totalDistanceCursor.moveToNext()){
            totalDistance = totalDistanceCursor.getInt(0);
        }

        totalStepsTextView.setText(String.valueOf(totalSteps));
        totalDistanceTextView.setText(String.valueOf(totalDistance));
    }

    public void setUsernameTextView(TextView usernameTextView) {
        this.usernameTextView = usernameTextView;
    }

    public void setProfilePictureView(ImageView profilePictureView) {
        this.profilePictureView = profilePictureView;
    }

    public void setLogsView(LogsListAdapter logsView) {
        this.logsView = logsView;
    }

    public void setTotalDistanceTextView(TextView totalDistanceTextView) {
        this.totalDistanceTextView = totalDistanceTextView;
    }

    public void setTotalStepsTextView(TextView totalStepsTextView) {
        this.totalStepsTextView = totalStepsTextView;
    }
}
