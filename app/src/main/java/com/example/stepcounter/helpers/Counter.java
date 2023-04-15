package com.example.stepcounter.helpers;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.stepcounter.data.Repository;

public class Counter implements SensorEventListener, LocationListener  {
    // counter variables
    private Sensor accelerometerSensor, stepCounterSensor;
    private SensorManager sensorManager;
    private LocationManager locationManager;
    private SensorsHandler sensorsHandler;
    private Location lastLocation;
    private long lastUpdateTime;
    private float totalDistance;
    private TextView stepsCountText, kilometersText, caloriesText, userAchievedHisGoalText;
    private double accel_previousMagnitude = 9;
    private double accel_nextMagnitude = 0;
    private static final long accel_COOLDOWN_PERIOD = 500;
    private long accel_lastStepTime = 0;
    private double accel_threshold = 10; // Peak detection threshold
    private static final float SPEED_THRESHOLD = 1.0f;
    private double alpha = 0.1; // Smoothing factor
    private static int stepCountAccel = 0;
    private static int stepCounterCount = 0;
    private int totalStepsCount = 0;
    private int savedSteps = 0;
    private int prevStepCount = 0;
    private String totalKilometers = "0.00", total_calories = "0.00";
    private float caloriesBurnt;
    private final float user_weight = 70, user_height = 185, user_age = 20;
    private int totalMinutes = 0;
    private CountDownTimer mCountDownTimer;
    private long startTime = 0;
    private long currentTime = 0;

    private long elapsedTime = 0;
    private boolean isCounting;
    private boolean inNormalSpeed;
    private static Counter counterInstance;
    private FragmentActivity activity;
    private String stopwatchValue;
    private Chronometer stopwatchText;
    private Stopwatch stopwatch;
    private boolean achievedGoal;

    private Counter() {
        this.isCounting = false;
        this.inNormalSpeed = true;
        this.sensorsHandler = new SensorsHandler();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            double x = event.values[0];
            double y = event.values[1];
            double z = event.values[2];

            double magnitude = Math.sqrt(x * x + y * y + z * z);
            double filteredMagnitude = alpha * magnitude + (1 - alpha) * accel_previousMagnitude;

            long now = System.currentTimeMillis();
            if (now - accel_lastStepTime > accel_COOLDOWN_PERIOD) {
                if (filteredMagnitude > accel_threshold && filteredMagnitude > accel_previousMagnitude && filteredMagnitude > accel_nextMagnitude && inNormalSpeed) {
                    stepCountAccel++;
                    accel_lastStepTime = now;
                    // Update UI with step count
                    setStepsCountTextContent();
                }
            }

            checkIfUserAchievedHisGoal();

            accel_nextMagnitude = filteredMagnitude;
            accel_previousMagnitude = accel_nextMagnitude;
        }

        else if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            int count = (int) event.values[0];
            Log.d("count", String.valueOf(event.values[0]));
            float diff_in_count = count - prevStepCount;
            if(count > stepCountAccel && prevStepCount > 0){
                stepCounterCount += diff_in_count;
            }
            setStepsCountTextContent();
            prevStepCount = count;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    public void toggleRegistrationSensor() {
        if (isCounting) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
            if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }
        } else {
            sensorManager.unregisterListener(this);
            if(locationManager != null){
                locationManager.removeUpdates(this);
            }
        }
    }

    public void checkIfUserAchievedHisGoal() {
        if(totalStepsCount >= Repository.getGoal()){
            userAchievedHisGoalText.setVisibility(VISIBLE);
            achievedGoal = true;
        } else {
            userAchievedHisGoalText.setVisibility(INVISIBLE);
            achievedGoal = false;
        }
    }

    public void toggleIsCounting() {
        this.isCounting = !this.isCounting;
        if(!isCounting) {
            saveToRepository();
        }
        toggleRegistrationSensor();
    }

    public boolean getIsCounting() {
        return isCounting;
    }

    public int getTotalStepsCount() {
        return totalStepsCount;
    }

    public float getTotalDistance() {
        return totalDistance;
    }
    public void setUserAchievedHisGoalText(TextView userAchievedHisGoalText) {
        this.userAchievedHisGoalText = userAchievedHisGoalText;
    }

    public void setAccelerometerSensor(Sensor accelerometerSensor) {
        this.accelerometerSensor = accelerometerSensor;
        this.sensorsHandler.setAccelerometerSensor(accelerometerSensor);
    }

    public void setStepCounterSensor(Sensor stepCounterSensor) {
        this.stepCounterSensor = stepCounterSensor;
        this.sensorsHandler.setStepCounterSensor(stepCounterSensor);
    }

    public void setSensorManager(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
        this.sensorsHandler.setSensorManager(sensorManager);
    }

    public void setStopwatchText(Chronometer stopwatchText) {
        this.stopwatchText = stopwatchText;
        setStopwatchTextContent();
    }

    public void setStopwatch(Stopwatch stopwatch) {
        this.stopwatch = stopwatch;
    }

    public void setStepsCountText(TextView stepsCountText){
        this.stepsCountText = stepsCountText;
        setStepsCountTextContent();
    }

    public void setKilometersText(TextView kilometersText) {
        this.kilometersText = kilometersText;
        setKMsTextContent();
    }

    public void setCaloriesText(TextView caloriesText) {
        this.caloriesText = caloriesText;
        setCaloriesTextContent();
    }

    public static Counter getCounterInstance() {
        if(counterInstance == null)
            counterInstance = new Counter();
        return counterInstance;
    }

    public void setLocationManager(LocationManager locationManager) {
        this.locationManager = locationManager;
        this.sensorsHandler.setLocationManager(locationManager);
    }

    public void setActivity(FragmentActivity activity) {
        this.activity = activity;
        this.sensorsHandler.setActivity(activity);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if(isCounting && totalStepsCount == 0){
            startTime = System.currentTimeMillis();
        }

        if(isCounting) {
            long currentTime = System.currentTimeMillis();

            if (lastLocation != null) {
                float distance = location.distanceTo(lastLocation);
                float speed = (distance / ((currentTime - lastUpdateTime) / 1000.0f));

                if (speed < SPEED_THRESHOLD && speed > 0.3f) {
                    totalDistance += distance;
                    inNormalSpeed = true;
               } else {
                    inNormalSpeed = false;
                }

                setKMsTextContent();
                setCaloriesTextContent();
            }
            lastLocation = location;

            lastUpdateTime = currentTime;
            Log.d("time", String.valueOf(lastUpdateTime));
        }
    }

    public void setStepsCountTextContent() {
        if(!isCounting) {
            savedSteps = Repository.getSteps();
            stepCountAccel = savedSteps;
        }

        totalStepsCount = Math.max(stepCountAccel, stepCounterCount);
        stepsCountText.setText(String.valueOf(totalStepsCount));
    }

    public void setKMsTextContent() {
        if(!isCounting){
            totalDistance = Repository.getDistance();
        }

        if(totalDistance > 0){
            totalKilometers = String.format("%.2f", (totalDistance / 1000.0f));
            kilometersText.setText(totalKilometers);
        }
    }

    public void setCaloriesTextContent() {
        if(!isCounting){
            totalDistance = Repository.getDistance();
        }

        if(totalDistance > 0) {
            caloriesBurnt = (100 * (totalDistance / 1000.0f));
            total_calories = String.format("%.2f", (caloriesBurnt));
            caloriesText.setText(total_calories);
        }
    }

    public void setStopwatchTextContent() {
        if(!isCounting){
            stopwatchValue = Repository.getTime();
            stopwatch.setSavedStopwatch(stopwatchValue);
            stopwatchText.setText(stopwatchValue);
        }
    }

    public void saveToRepository() {
        Repository.setSteps(totalStepsCount);
        Repository.setDistance(totalDistance);
        Repository.setTime(stopwatch.getStopwatchCount());
    }

    public boolean getAchievedGoal() {
        return achievedGoal;
    }

    public static void resetCounter() {
        stepCountAccel = 0;
        stepCounterCount = 0;
    }
}