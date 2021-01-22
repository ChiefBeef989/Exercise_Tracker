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

    private static ExerciseTracker instance;

    Activity mActivity;

    LocationManager locationManager;
    long startTime, endTime;
    public List<LocationMarker> locationMarkers;

    File gpxFile;
    FileWriter fileWriter;
    BufferedWriter bufferedWriter;

    public ExerciseTracker(Activity activity) {
        this.mActivity = activity;
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        locationMarkers = new ArrayList<LocationMarker>();
        instance = this;
    }


    @Override
    public void run() {
        super.run();
        Log.i("LocationManager", "START");

        startTime = SystemClock.elapsedRealtimeNanos();

        Log.i("LocationManager", "PERMISSIONS GRANTED");
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this, Looper.getMainLooper());

        File root = new File("/sdcard/GPSTracks");
        root.mkdirs();
        gpxFile = new File(root,Date.from(Instant.now()).toString()+".gpx");

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
        locationManager.removeUpdates(this);
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


    @Override
    public void onLocationChanged(@NonNull Location location) {
        float latitude = (float)location.getLatitude();
        float longitude = (float)location.getLongitude();
        float altitude = (float)location.getAltitude();

        LocalDateTime localDateTime = LocalDateTime.now();
        locationMarkers.add(new LocationMarker(latitude,longitude,altitude,localDateTime));

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

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    private void permissionCheck(){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    Log.i("LocationManager","PERMISSIONS DENIED");

                    ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.ACCESS_FINE_LOCATION);
                    ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.ACCESS_FINE_LOCATION);
                    Log.i("LocationManager","PERMISSIONS GRANTED BY USER");
                }
            }
        });
    }

    public static ExerciseTracker getInstance(Activity mActivity){
        if(instance == null){
            instance = new ExerciseTracker(mActivity);
        }
        return instance;
    }
}
