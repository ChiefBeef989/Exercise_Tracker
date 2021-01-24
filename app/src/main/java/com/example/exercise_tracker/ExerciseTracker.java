package com.example.exercise_tracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ExerciseTracker extends Thread implements LocationListener {

    //singleton so all activities can access data
    private static ExerciseTracker instance;

    //activity needed to access system services/permissions
    Activity mActivity;

    //variables related to exercise
    LocationManager locationManager;
    public List<LocationMarker> locationMarkers;

    //variables related to creating/editing files
    File gpxFile;
    FileWriter fileWriter;
    BufferedWriter bufferedWriter;

    //initialize fields and singleton
    public ExerciseTracker(Activity activity) {
        this.mActivity = activity;
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        locationMarkers = new ArrayList<LocationMarker>();
        instance = this;
    }

    @Override
    public void run() {
        super.run();

        //return if necessary permissions have not been granted
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //set interval for requesting location to 5 seconds
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this, Looper.getMainLooper());

        //create .gpx file and directory
        //access file in Android Studio by going to View->Tools->Device file explorer->/sdcard/GPSTracks/
        File root = new File("/sdcard/GPSTracks");
        root.mkdirs();
        gpxFile = new File(root,Date.from(Instant.now()).toString()+".gpx");

        //create gpx structure in file
        try{
            fileWriter = new FileWriter(gpxFile,true);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("<gpx>");
            bufferedWriter.newLine();
            bufferedWriter.write("\t<trk>");
            bufferedWriter.newLine();
            bufferedWriter.write("\t\t<name>Exercise_Tracker</name>");
            bufferedWriter.newLine();
            bufferedWriter.write("\t\t<trkseg>");
            bufferedWriter.newLine();
            bufferedWriter.flush();
            Log.i("GPX file saved under ",gpxFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void endRecording(){
        //stop requesting location updates
        locationManager.removeUpdates(this);

        //write closing tags into gpx file
        try{
            bufferedWriter.write("\t\t</trkseg>");
            bufferedWriter.newLine();
            bufferedWriter.write("\t</trk>");
            bufferedWriter.newLine();
            bufferedWriter.write("</gpx>");
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * override method from LocationListener interface
     * @param location
     */
    @Override
    public void onLocationChanged(@NonNull Location location) {
        //save location data to variables
        float latitude = (float)location.getLatitude();
        float longitude = (float)location.getLongitude();
        float altitude = (float)location.getAltitude();

        //create timestamp and new location marker and add it to a list
        LocalDateTime localDateTime = LocalDateTime.now();
        LocationMarker tmpLocation = new LocationMarker(latitude,longitude,altitude,localDateTime);
        locationMarkers.add(tmpLocation);

        //calculate distance in meters to last location marker (skips first marker)
        if(locationMarkers.size() > 1)
            tmpLocation.setDistToLastLocation(tmpLocation.calculateDistance(locationMarkers.get(locationMarkers.indexOf(tmpLocation)-1)));

        //add <trkpt> element to gpx file
        try{
            bufferedWriter.write("\t\t\t<trkpt lat=\"" + latitude + "\" long=\"" + longitude + "\">");
            bufferedWriter.newLine();
            bufferedWriter.write("\t\t\t\t<ele>"+altitude+"</ele>");
            bufferedWriter.newLine();
            bufferedWriter.write("\t\t\t\t<time>"+localDateTime.format(DateTimeFormatter.ISO_DATE_TIME)+"</time>");
            bufferedWriter.newLine();
            bufferedWriter.write("\t\t\t</trkpt>");
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("MARKER", "\n" + locationMarkers.get(locationMarkers.size()-1).toString());
    }

    //LocationListener methods not used by this class
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    /**
     * @param mActivity
     * @return Exercise Tracker instance. Create a new one if no instance has been initialized yet
     */
    public static ExerciseTracker getInstance(Activity mActivity){
        if(instance == null){
            instance = new ExerciseTracker(mActivity);
        }
        return instance;
    }
}
