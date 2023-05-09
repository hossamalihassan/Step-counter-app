package com.example.stepcounter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.stepcounter.activities.Onboarding;
import com.example.stepcounter.data.Repository;
import com.example.stepcounter.databinding.ActivityMainBinding;
import com.example.stepcounter.fragments.HomeFragment;
import com.example.stepcounter.fragments.SettingsFragment;
import com.example.stepcounter.fragments.ProfileFragment;
import com.example.stepcounter.helpers.Counter;
import com.example.stepcounter.helpers.DatabaseHelper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Repository.getInstance(getApplicationContext()); // create repository instance
        if(Objects.equals(Repository.getUsername(), "")){
            Intent intent = new Intent(this, Onboarding.class);
            startActivity(intent);
        }

        // ask for permission
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        changeFragment(HomeFragment.getHomeFragmentInstance()); // set default fragment

        DatabaseHelper.getDatabaseHelperInstance(this);
        if(!DatabaseHelper.didTheUserWorkoutToday()) {
            Repository.setTime("00:00");
            Repository.setDistance(0);
            Repository.setSteps(0);
            Counter.resetCounter();
        }

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    changeFragment(HomeFragment.getHomeFragmentInstance());
                    break;
                case R.id.profile:
                    changeFragment(ProfileFragment.getProfileFragmentInstance());
                    break;
                case R.id.settings:
                    changeFragment(SettingsFragment.getSettingsFragmentInstance());
                    break;
            }
            return true;
        });
    }

    private void changeFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}