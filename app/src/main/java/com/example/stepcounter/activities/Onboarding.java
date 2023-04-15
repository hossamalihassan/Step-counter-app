package com.example.stepcounter.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.stepcounter.R;
import com.example.stepcounter.helpers.CameraHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class Onboarding extends AppCompatActivity {

    CameraHandler cameraHandler;
    private final int CAMERA_REQUEST_CODE = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        cameraHandler = new CameraHandler();
        cameraHandler.setActivity(this);
        cameraHandler.getPermissions();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void takeAPhoto(View v) {
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
}