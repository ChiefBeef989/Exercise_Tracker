package com.example.exercise_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
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
        ExerciseReport exerciseReport = new ExerciseReport(ExerciseTracker.getInstance(MainActivity.getInstance()).locationMarkers);

        //set colors of text views according to whether or not the user goals have been achieved
        if(MainActivity.getInstance().getDistanceGoal() <= 0)
            distance.setTextColor(Color.WHITE);
        else if(exerciseReport.getTotal_distance() < MainActivity.getInstance().getDistanceGoal())
            distance.setTextColor(Color.RED);
        else
            distance.setTextColor(Color.GREEN);

        if(MainActivity.getInstance().getPaceGoal() <= 0)
            avg_speed.setTextColor(Color.WHITE);
        else if(exerciseReport.getAvgSpeed() < MainActivity.getInstance().getPaceGoal())
            avg_speed.setTextColor(Color.RED);
        else
            avg_speed.setTextColor(Color.GREEN);

        //assign texts to TextViews
        duration.setText(exerciseReport.getExerciseTime()+"s");
        distance.setText(exerciseReport.getTotal_distance()+"m");
        avg_speed.setText(exerciseReport.getAvgSpeed()+"km/h");
        min_alt.setText(exerciseReport.getMinAlt()+"m");
        max_alt.setText(exerciseReport.getMaxAlt()+"m");
    }
}