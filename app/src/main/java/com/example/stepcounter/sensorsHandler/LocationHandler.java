package com.example.stepcounter.sensorsHandler;

import android.location.Location;

public class LocationHandler {

    private Location lastLocation;
    private long lastUpdateTime;
    private static final float SPEED_THRESHOLD = 1.0f;
    private static LocationHandler locationHandlerInstance;
    private boolean inNormalSpeed;
    private float totalDistance;
    private LocationHandler() {

    }

    public void getLocation(Location location) {
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
        }
        lastLocation = location;
        lastUpdateTime = currentTime;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public boolean getInNormalSpeed() {
        return inNormalSpeed;
    }

    public float getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(float totalDistance) {
        this.totalDistance = totalDistance;
    }

    public static LocationHandler getLocationHandlerInstance() {
        if(locationHandlerInstance == null){
            locationHandlerInstance = new LocationHandler();
        }
        return locationHandlerInstance;
    }
}
