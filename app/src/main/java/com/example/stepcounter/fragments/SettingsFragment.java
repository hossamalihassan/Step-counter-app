package com.example.stepcounter.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.stepcounter.R;
import com.example.stepcounter.data.Repository;
import com.example.stepcounter.helpers.SettingsHandler;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SettingsHandler settingsHandler;
    private static SettingsFragment settingsFragmentInstance;
    private SettingsFragment() {
        settingsHandler = new SettingsHandler();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        TextView goalInput = view.findViewById(R.id.goalInput);
        settingsHandler.setGoalInput(goalInput);
        settingsHandler.setActivity(getActivity());
        settingsHandler.createNotificationChannel();

        Button setGoalBtn = (Button) view.findViewById(R.id.setGoalBtn);
        setGoalBtn.setOnClickListener(v -> {
            settingsHandler.updateGoal();
        });

        TextView reminderTimePicked = (TextView) view.findViewById(R.id.reminderTimePicked);
        Button setDilayReminderBtn = (Button) view.findViewById(R.id.pickATime);
        reminderTimePicked.setText(Repository.getDailyReminder());
        setDilayReminderBtn.setOnClickListener(v -> {
            settingsHandler.showDailyReminderTimeDialog(reminderTimePicked);
        });

        Button resetCounterBtn = (Button) view.findViewById(R.id.resetCounterBtn);
        resetCounterBtn.setOnClickListener(v -> {
            settingsHandler.resetCounter();
        });

        return view;
    }


    public static SettingsFragment getSettingsFragmentInstance() {
        if(settingsFragmentInstance == null) {
            settingsFragmentInstance = new SettingsFragment();
        }
        return settingsFragmentInstance;
    }
}