package com.example.exercise_tracker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocationMarker {
    private float latitude;
    private float longitude;
    private float altitude;
    private float distToLastLocation;
    private LocalDateTime timeStamp;

    public LocationMarker(float latitude, float longitude, float altitude, LocalDateTime timeStamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.timeStamp = timeStamp;
        this.distToLastLocation = 0;
    }

    /**
     * Uses Haversine formula to calculate the distance between two coordinates on a sphere with the radius of earth
     * Source: https://stackoverflow.com/a/27943
     * last accessed 22.01.2021 21:42 (UTC)
     * @param other locationMarker to calculate the distance to
     * @return distance between to locations in meters
     */
    public float calculateDistance(LocationMarker other){
        int r = 6371;

        float distanceLatitudeRad = (this.latitude - other.latitude) * (float)(Math.PI/180);
        float distanceLongitudeRad = (this.longitude - other.longitude) * (float)(Math.PI/180);

        float a = (float) (Math.sin(distanceLatitudeRad/2) * Math.sin(distanceLatitudeRad/2) +
                Math.cos(this.latitude * (Math.PI/180)) * Math.cos(other.latitude * (Math.PI/180)) *
                        Math.sin(distanceLongitudeRad/2) * Math.sin(distanceLongitudeRad/2));

        float c = (float) (2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a)));

        float distance = r*c*1000;

        return distance;
    }

    public float getDistToLastLocation() {
        return distToLastLocation;
    }

    public void setDistToLastLocation(float distToLastLocation) {
        this.distToLastLocation = distToLastLocation;
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
        return "Latitude: " + latitude + "\nLongitude: " + longitude + "\n" + "Altitude: " + altitude + "\nTime: " + timeStamp.format(DateTimeFormatter.ISO_DATE_TIME) + "\nDistance to last location in m: " + distToLastLocation;
    }
}
