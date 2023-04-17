package com.example.stepcounter.sensorsHandler;

public class AccelerometerSensorHandler {

    private double accel_previousMagnitude = 9;
    private double accel_nextMagnitude = 0;
    private static final long accel_COOLDOWN_PERIOD = 500;
    private long accel_lastStepTime = 0;
    private double accel_threshold = 10; // Peak detection threshold
    private double alpha = 0.1; // Smoothing factor
    private static int stepCountAccel = 0;
    private boolean inNormalSpeed;

    private static AccelerometerSensorHandler accelerometerSensorHandlerInstance;
    private AccelerometerSensorHandler() {

    }

    public void getAccelerometerCount(double x, double y, double z) {
        double magnitude = Math.sqrt(x * x + y * y + z * z);
        double filteredMagnitude = alpha * magnitude + (1 - alpha) * accel_previousMagnitude;

        long now = System.currentTimeMillis();
        if (now - accel_lastStepTime > accel_COOLDOWN_PERIOD) {
            if (filteredMagnitude > accel_threshold && filteredMagnitude > accel_previousMagnitude && filteredMagnitude > accel_nextMagnitude && inNormalSpeed) {
                stepCountAccel++;
                accel_lastStepTime = now;
                // Update UI with step count
            }
        }

        accel_nextMagnitude = filteredMagnitude;
        accel_previousMagnitude = accel_nextMagnitude;
    }

    public int getStepCountAccel() {
        return stepCountAccel;
    }

    public void setInNormalSpeed(boolean inNormalSpeed) {
        this.inNormalSpeed = inNormalSpeed;
    }

    public static AccelerometerSensorHandler getAccelerometerSensorHandlerInstance() {
        if(accelerometerSensorHandlerInstance == null){
            accelerometerSensorHandlerInstance = new AccelerometerSensorHandler();
        }
        return accelerometerSensorHandlerInstance;
    }
}
