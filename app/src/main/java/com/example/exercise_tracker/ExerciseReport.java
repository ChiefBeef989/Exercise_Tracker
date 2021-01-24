package com.example.exercise_tracker;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class ExerciseReport {
    private float avgSpeed;
    private float total_distance;
    private float minAlt;
    private float maxAlt;
    private long exerciseTime;

    /**
     * Creates an ExerciseReport object using the location markers created during the recording
     * @param locationMarkers
     */
    public ExerciseReport(List<LocationMarker> locationMarkers){
        //subtract first timestamp from current time
        exerciseTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - locationMarkers.get(0).getTimeStamp().toEpochSecond(ZoneOffset.UTC);

        minAlt = locationMarkers.get(0).getAltitude();
        maxAlt = minAlt;

        //loop through all timestamps to get total distance as well as min/max altitude
        for (int i = 0; i < locationMarkers.size(); i++) {
            total_distance += locationMarkers.get(i).getDistToLastLocation();
            if(locationMarkers.get(i).getAltitude() < minAlt)
                minAlt = locationMarkers.get(i).getAltitude();
            else if(locationMarkers.get(i).getAltitude() > maxAlt)
                maxAlt = locationMarkers.get(i).getAltitude();
        }

        //calculate total average speed (km/h)
        float avg_ms = total_distance/exerciseTime;
        avgSpeed = avg_ms*3.6f;
    }

    //basic getters
    public float getAvgSpeed() {
        return avgSpeed;
    }

    public float getTotal_distance() {
        return total_distance;
    }

    public float getMinAlt() {
        return minAlt;
    }

    public float getMaxAlt() {
        return maxAlt;
    }

    public long getExerciseTime() {
        return exerciseTime;
    }
}
