package com.example.exercise_tracker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocationMarker {
    private float latitude;
    private float longitude;
    private float altitude;
    private LocalDateTime timeStamp;

    public LocationMarker(float latitude, float longitude, float altitude, LocalDateTime timeStamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.timeStamp = timeStamp;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getAltitude() {
        return altitude;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public String toString(){
        return "Latitude: " + latitude + "\nLongitude: " + longitude + "\n" + "Altitude: " + altitude + "\nTime: " + timeStamp.format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
