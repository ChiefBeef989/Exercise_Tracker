package com.example.exercise_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

/**
 * Activity to display information about the completed exercise
 */
public class ExerciseResultActivity extends AppCompatActivity {

    private TextView duration, distance, avg_speed, min_alt, max_alt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_result);

        //find and initialize TextView variables
        duration = (TextView) findViewById(R.id.durationText);
        distance = (TextView) findViewById(R.id.distanceText);
        avg_speed = (TextView) findViewById(R.id.avgSpeedText);
        min_alt = (TextView) findViewById(R.id.minAltText);
        max_alt = (TextView) findViewById(R.id.maxAltText);

        //create Exercise Report object based on location markers
        ExerciseReport exerciseReport = new ExerciseReport(ExerciseTracker.getInstance(this).locationMarkers);

        //assign texts to TextViews
        duration.setText(exerciseReport.getExerciseTime()+"s");
        distance.setText(exerciseReport.getTotal_distance()+"m");
        avg_speed.setText(exerciseReport.getAvg_speed()+"km/h");
        min_alt.setText(exerciseReport.getMin_alt()+"m");
        max_alt.setText(exerciseReport.getMax_alt()+"m");
    }
}