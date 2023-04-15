package com.example.stepcounter.helpers;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stepcounter.data.Repository;

public class OnboardingHandler {

    private TextView usernameTextView, goalTextView;
    private String profilePicPath;
    private String username;
    private int goal;
    private Activity activity;

    public OnboardingHandler() {}

    public void registerUser() {
        username = usernameTextView.getText().toString();
        goal = Integer.parseInt(goalTextView.getText().toString());

        if(validUsername() && validGoal()){
            Repository.setUsername(username);
            Repository.setGoal(goal);
            Repository.setProfilePicPath(profilePicPath);
            Log.d("you are", "in");
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
    }

    public void setUsernameTextView(TextView usernameTextView) {
        this.usernameTextView = usernameTextView;
    }

    public void setProfilePicPath(String profilePicPath) {
        this.profilePicPath = profilePicPath;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
