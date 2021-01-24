package com.example.exercise_tracker;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class ExerciseReport {
    private float avg_speed;
    private float total_distance;
    private float min_alt;
    private float max_alt;
    private long exerciseTime;

    /**
     * Creates an ExerciseReport object using the location markers created during the recording
     * @param locationMarkers
     */
    public ExerciseReport(List<LocationMarker> locationMarkers){
        //subtract first timestamp from current time
        exerciseTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - locationMarkers.get(0).getTimeStamp().toEpochSecond(ZoneOffset.UTC);

        min_alt = locationMarkers.get(0).getAltitude();
        max_alt = min_alt;

        //loop through all timestamps to get total distance as well as min/max altitude
        for (int i = 0; i < locationMarkers.size(); i++) {
            total_distance += locationMarkers.get(i).getDistToLastLocation();
            if(locationMarkers.get(i).getAltitude() < min_alt)
                min_alt = locationMarkers.get(i).getAltitude();
            else if(locationMarkers.get(i).getAltitude() > max_alt)
                max_alt = locationMarkers.get(i).getAltitude();
        }

        //calculate total average speed (km/h)
        float avg_ms = total_distance/exerciseTime;
        avg_speed = avg_ms*3.6f;
    }

    //basic getters
    public float getAvg_speed() {
        return avg_speed;
    }

    public float getTotal_distance() {
        return total_distance;
    }

    public float getMin_alt() {
        return min_alt;
    }

    public float getMax_alt() {
        return max_alt;
    }

    public long getExerciseTime() {
        return exerciseTime;
    }
}
