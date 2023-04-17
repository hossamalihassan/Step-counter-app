package com.example.stepcounter.sensorsHandler;

public class StepCounterSensorHandler {

    private static int stepCounterCount = 0;
    private int totalStepsCount = 0;
    private int savedSteps = 0;
    private int prevStepCount = 0;
    private static StepCounterSensorHandler stepCounterSensorHandlerInstance;
    private StepCounterSensorHandler() {

    }

    public void getStepCounterCount(int count, int stepCountAccel) {
        float diff_in_count = count - prevStepCount;
        if(count > stepCountAccel && prevStepCount > 0){
            stepCounterCount += diff_in_count;
        }
        prevStepCount = count;
    }

    public int getStepCounterCount() {
        return stepCounterCount;
    }

    public static StepCounterSensorHandler getStepCounterSensorHandlerInstance() {
        if(stepCounterSensorHandlerInstance == null){
            stepCounterSensorHandlerInstance = new StepCounterSensorHandler();
        }
        return stepCounterSensorHandlerInstance;
    }
}
