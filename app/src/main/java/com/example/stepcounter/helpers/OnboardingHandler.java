package com.example.stepcounter.helpers;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stepcounter.data.Repository;
import com.example.stepcounter.fragments.HomeFragment;

public class OnboardingHandler {

    private TextView usernameTextView, goalTextView;
    private String profilePicPath;
    private String username;
    private int goal;
    private Activity activity;

    public OnboardingHandler() {}

    public void registerUser() {
        if(validUsername() && validGoal()){
            Repository.setUsername(username);
            Repository.setGoal(goal);
            Repository.setProfilePicPath(profilePicPath);
            activity.finish();
        }
    }

    public boolean validUsername() {
        if(!username.equals("")){
            return true;
        }
        Toast.makeText(activity.getApplicationContext(), "Username can't be empty", Toast.LENGTH_SHORT).show();
        return false;
    }

    public boolean validGoal() {
        if(goal < 999999999){
            return true;
        }
        Toast.makeText(activity.getApplicationContext(), "Steps count can't exceed 999999999", Toast.LENGTH_SHORT).show();
        return false;
    }

    public void setGoalTextView(TextView goalTextView) {
        this.goalTextView = goalTextView;
        setGoal(Integer.parseInt(this.goalTextView.getText().toString()));
    }

    public void setUsernameTextView(TextView usernameTextView) {
        this.usernameTextView = usernameTextView;
        setUsername(this.usernameTextView.getText().toString());
    }

    public void setProfilePicPath(String profilePicPath) {
        this.profilePicPath = profilePicPath;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Activity getActivity() {
        return activity;
    }

    public String getUsername() {
        return username;
    }

    public int getGoal() {
        return goal;
    }

    public String getProfilePicPath() {
        return profilePicPath;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }
}
