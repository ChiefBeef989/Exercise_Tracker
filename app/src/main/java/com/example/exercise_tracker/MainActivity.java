package com.example.exercise_tracker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private Button startEndButton;
    private TextView distanceGoalView;
    private TextView paceGoalView;

    private int distanceGoal;
    private int paceGoal;

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

        //initialize start/stop button and goal text views
        startEndButton = (Button) findViewById(R.id.btnStartEndWalk);
        distanceGoalView = (TextView) findViewById(R.id.distanceGoalInput);
        paceGoalView = (TextView) findViewById(R.id.paceGoalInput);

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
                    //save user goals and start tracker thread if user is not yet walking
                    if(distanceGoalView.getText().toString().isEmpty())
                        distanceGoal = 0;
                    else
                        distanceGoal = Integer.parseInt(distanceGoalView.getText().toString());

                    if(paceGoalView.getText().toString().isEmpty())
                        paceGoal = 0;
                    else
                        paceGoal = Integer.parseInt(paceGoalView.getText().toString());

                    startEndButton.setText(R.string.end);
                    tracker.start();
                }
                //invert "status" boolean
                walking = !walking;
            }
        });

    }

    public int getDistanceGoal() {
        return distanceGoal;
    }

    public int getPaceGoal() {
        return paceGoal;
    }

    //return singleton instance
    public static MainActivity getInstance(){
        if(instance == null){
            instance = new MainActivity();
        }
        return instance;
    }
}