package com.example.stepcounter.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stepcounter.R;
import com.example.stepcounter.helpers.CameraHandler;
import com.example.stepcounter.helpers.OnboardingHandler;

import java.io.IOException;

public class Onboarding extends AppCompatActivity {

    private long takenAt;
    private CameraHandler cameraHandler;
    private OnboardingHandler onboardingHandler;
    private final int CAMERA_REQUEST_CODE = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        cameraHandler = new CameraHandler();
        onboardingHandler = new OnboardingHandler();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void takeAPhoto(View v) {
        // handles taking profile picture
        takenAt = System.currentTimeMillis();

        cameraHandler.setActivity(this);
        cameraHandler.setTakenAt(takenAt);

        Uri imgPath = null;
        try {
            imgPath = cameraHandler.createProfilePicture();
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgPath);
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            ImageView profilePicView = (ImageView) findViewById(R.id.profilePicView);
            profilePicView.setImageURI(cameraHandler.getImageUri());
        }
    }

    public void registerUser(View v) {
        // handle user inputs
        TextView usernameTextView = (TextView) findViewById(R.id.setNameText);
        onboardingHandler.setUsernameTextView(usernameTextView);
        TextView goalTextView = (TextView) findViewById(R.id.setGoalText);
        onboardingHandler.setGoalTextView(goalTextView);

        onboardingHandler.setProfilePicPath(cameraHandler.getImgName());
        onboardingHandler.setActivity(this);

        onboardingHandler.registerUser();
    }
}