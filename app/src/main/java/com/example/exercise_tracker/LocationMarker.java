package com.example.exercise_tracker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class to save location data later used to create an exercise report
 */
public class LocationMarker {
    //location data to be saved
    private float latitude;
    private float longitude;
    private float altitude;
    private float distToLastLocation;
    private float paceToLastLocation;
    private LocalDateTime timeStamp;

    /**
     * basic constructor
     * @param latitude
     * @param longitude
     * @param altitude
     * @param timeStamp
     */
    public LocationMarker(float latitude, float longitude, float altitude, LocalDateTime timeStamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.timeStamp = timeStamp;
        this.distToLastLocation = 0f;
        this.paceToLastLocation = 0f;
    }

    /**
     * Uses Haversine formula to calculate the distance between two coordinates on a sphere with the radius of earth
     * Sources:
     * https://stackoverflow.com/a/27943, last accessed 22.01.2021 21:42 (UTC)
     * http://www.movable-type.co.uk/scripts/latlong.html, last accessed 24.01.2021 15:34 (UTC)
     * @param other locationMarker to calculate the distance to
     * @return distance between to locations in meters
     */
    public float calculateDistance(LocationMarker other){
        //approximate radius of earth in km
        int r = 6371;

        //convert decimals to radians
        float distanceLatitudeRad = (this.latitude - other.latitude) * (float)(Math.PI/180);
        float distanceLongitudeRad = (this.longitude - other.longitude) * (float)(Math.PI/180);

        float a = (float) (Math.sin(distanceLatitudeRad/2) * Math.sin(distanceLatitudeRad/2) +
                Math.cos(this.latitude * (Math.PI/180)) * Math.cos(other.latitude * (Math.PI/180)) *
                        Math.sin(distanceLongitudeRad/2) * Math.sin(distanceLongitudeRad/2));

        float c = (float) (2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a)));

        float distance = r*c*1000;

        return distance;
    }

    //set distance to last location and calculate pace
    public void setDistAndPaceToLastLocation(float distToLastLocation) {
        this.distToLastLocation = distToLastLocation;
        this.paceToLastLocation = (distToLastLocation/5)*3.6f;
    }

    //basic getters
    public float getDistToLastLocation() {
        return distToLastLocation;
    }

    public float getPaceToLastLocation() {
        return paceToLastLocation;
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

    //toString (only used for Log.i output)
    public String toString(){
        return "Latitude: " + latitude + "\nLongitude: " + longitude + "\n" + "Altitude: " + altitude + "\nTime: " + timeStamp.format(DateTimeFormatter.ISO_DATE_TIME) + "\nDistance to last location in m: " + distToLastLocation;
    }
}
