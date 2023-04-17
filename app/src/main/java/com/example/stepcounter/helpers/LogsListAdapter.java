package com.example.stepcounter.helpers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stepcounter.R;

import java.util.ArrayList;

public class LogsListAdapter extends RecyclerView.Adapter<LogsListAdapter.LogHolder> {

    ArrayList<String> log_date;
    ArrayList<Integer> log_steps;

    @NonNull
    @Override
    public LogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LogHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.logs_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LogHolder holder, int position) {
        holder.day.setText(log_date.get(position));
        holder.stepsCount.setText(String.valueOf(log_steps.get(position)));
    }

    @Override
    public int getItemCount() {
        return log_date.size();
    }

    public void setLog_date(ArrayList<String> log_date) {
        this.log_date = log_date;
    }

    public void setLog_steps(ArrayList<Integer> log_steps) {
        this.log_steps = log_steps;
    }

    public class LogHolder extends RecyclerView.ViewHolder {

        TextView day;
        TextView stepsCount;
        public LogHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.logDateItem);
            stepsCount = itemView.findViewById(R.id.logStepsCountItem);
        }
    }
}
