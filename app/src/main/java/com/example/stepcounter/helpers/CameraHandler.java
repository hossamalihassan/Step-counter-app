package com.example.stepcounter.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import java.io.IOException;

public class CameraHandler {

    private long takenAt;
    Activity activity;
    Uri imageUri;
    private final String IMG_PATH = "Pictures/StepCounter/";
    private String imgName;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public Uri createProfilePicture() throws IOException {
        Uri uri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);;
        ContentResolver contentResolver = activity.getContentResolver();

        imgName = "StepCounterProfilePic-"+ takenAt +".jpg";
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, imgName);
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, IMG_PATH);
        Uri finalUri = contentResolver.insert(uri, contentValues);
        imageUri = finalUri;
        return finalUri;
    }

    public void getPermissions() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
    }

    public void setTakenAt(long takenAt) {
        this.takenAt = takenAt;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public String getImgName() {
        return imgName;
    }
}
