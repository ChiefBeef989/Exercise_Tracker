package com.example.exercise_tracker;

import java.time.ZoneOffset;
import java.util.List;

public class ExerciseReport {
    private float avg_speed;
    private float total_distance;
    private float min_alt;
    private float max_alt;
    private long exerciseTime;

    public ExerciseReport(List<LocationMarker> locationMarkers){
        min_alt = locationMarkers.get(0).getAltitude();
        max_alt = min_alt;
        for (int i = 0; i < locationMarkers.size(); i++) {
            total_distance += locationMarkers.get(i).getDistToLastLocation();
            if(locationMarkers.get(i).getAltitude() < min_alt)
                min_alt = locationMarkers.get(i).getAltitude();
            else if(locationMarkers.get(i).getAltitude() > max_alt)
                max_alt = locationMarkers.get(i).getAltitude();
        }
        exerciseTime = locationMarkers.get(locationMarkers.size()-1).getTimeStamp().toEpochSecond(ZoneOffset.UTC) - locationMarkers.get(0).getTimeStamp().toEpochSecond(ZoneOffset.UTC);

        float avg_ms = total_distance/exerciseTime;
        avg_speed = avg_ms*3.6f;
    }

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
