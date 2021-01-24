package com.example.exercise_tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Button startEndButton;

    private boolean walking;

    //singleton
    private static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //assign instance
        instance = this;

        //check all needed permissions. Perform permission request if necessary
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            Log.i("LocationManager","PERMISSIONS DENIED");

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},0);
            Log.i("LocationManager","PERMISSIONS GRANTED BY USER");
        }

        //Create Exercise Tracker
        ExerciseTracker tracker = ExerciseTracker.getInstance(MainActivity.this);

        //initialize start/stop button
        startEndButton = (Button) findViewById(R.id.btnStartEndWalk);

        //assign onClick behavior
        startEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(walking){
                    //stop recording and start result activity if user is already walking
                    startEndButton.setText(R.string.start);
                    Intent startResults = new Intent(MainActivity.this, ExerciseResultActivity.class);
                    tracker.endRecording();
                    MainActivity.this.startActivity(startResults);
                } else {
                    //start tracker thread if user is not yet walking
                    startEndButton.setText(R.string.end);
                    tracker.start();
                }
                //invert "status" boolean
                walking = !walking;
            }
        });

    }

    //return singleton instance
    public static MainActivity getInstance(){
        if(instance == null){
            instance = new MainActivity();
        }
        return instance;
    }
}