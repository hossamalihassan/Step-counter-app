package com.example.stepcounter.fragments;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.SENSOR_SERVICE;

import com.example.stepcounter.data.Repository;
import com.example.stepcounter.helpers.Counter;
import com.example.stepcounter.R;
import com.example.stepcounter.helpers.DatabaseHelper;
import com.example.stepcounter.helpers.Stopwatch;
import com.example.stepcounter.data.SharedPreferencesHandler;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.icu.util.Calendar;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    SensorManager sensorManager;
    LocationManager locationManager;
    Sensor accelerometerSensor;
    Sensor stepCounterSensor;
    Counter counter;
    Stopwatch stopwatch;
    Chronometer stopwatchText;
    private static HomeFragment HomeFragmentInstance;
    private DatabaseHelper databaseHelper;

    private HomeFragment() {
        this.counter = Counter.getCounterInstance();
        this.stopwatch = new Stopwatch();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get sensor
        this.sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        this.locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        this.stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        this.accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        counter.setAccelerometerSensor(accelerometerSensor);
        counter.setStepCounterSensor(stepCounterSensor);
        counter.setSensorManager(sensorManager);
        counter.setLocationManager(locationManager);
        counter.setActivity(getActivity());

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    static TextView usernameGreeting;
    static TextView goalToAchieveText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView stepsCountText = (TextView) view.findViewById((R.id.stepsCount));
        counter.setStepsCountText(stepsCountText);

        TextView kilometersText = (TextView) view.findViewById((R.id.kilometers));
        counter.setKilometersText(kilometersText);

        TextView caloriesText = (TextView) view.findViewById((R.id.calories));
        counter.setCaloriesText(caloriesText);

        TextView greetingText = (TextView) view.findViewById((R.id.greeting));
        changeGreeting(greetingText);

        stopwatchText = (Chronometer) view.findViewById(R.id.chronometer);
        stopwatch.setChronometer(stopwatchText);
        counter.setStopwatch(stopwatch);
        counter.setStopwatchText(stopwatchText);

        TextView userAchievedHisGoalText = (TextView) view.findViewById((R.id.userAchievedHisGoalText));
        counter.setUserAchievedHisGoalText(userAchievedHisGoalText);

        goalToAchieveText = (TextView) view.findViewById((R.id.goalToAchieve));
        setGoalToAchieveTextContent(goalToAchieveText);
        counter.checkIfUserAchievedHisGoal();

        usernameGreeting = (TextView) view.findViewById((R.id.usernameGreeting));
        usernameGreeting.setText(Repository.getUsername());

        Button startBtn = (Button) view.findViewById(R.id.startBtn);
        startOrStop(startBtn);
        startBtn.setOnClickListener(v -> {
            counter.toggleIsCounting();
            startOrStop(startBtn);
        });
        return view;
    }

    public void startOrStop(Button startBtn){
        if(counter.getIsCounting()){
            stopwatch.startCounting();
            startBtn.setText(R.string.stopBtnText);
        } else {
            stopwatch.stopCounting();
            startBtn.setText(R.string.startBtnText);
        }
    }

    public static HomeFragment getHomeFragmentInstance() {
        if(HomeFragmentInstance == null){
            HomeFragmentInstance = new HomeFragment();
        }
        return HomeFragmentInstance;
    }

    public void setGoalToAchieveTextContent(TextView goalToAchieveText) {
        String goalToAchieveTextContent = "/" + Repository.getGoal();
        goalToAchieveText.setText(goalToAchieveTextContent);
    }

    public void changeGreeting(TextView greetingText) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if(hour > 12){
            greetingText.setText(R.string.goodEvening);
        } else {
            greetingText.setText(R.string.goodMorning);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(counter.getIsCounting()) {
            saveToRepository();
        }
    }

    public void saveToRepository() {
        Repository.setSteps(counter.getTotalStepsCount());
        Repository.setDistance(counter.getTotalDistance());
        Repository.setTime(stopwatch.getStopwatchCount());
    }

    public static TextView getUsernameGreeting() {
        return usernameGreeting;
    }

    public static TextView getGoalToAchieveText() {
        return goalToAchieveText;
    }
}