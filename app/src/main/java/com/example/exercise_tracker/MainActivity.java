package com.example.exercise_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
//Marlon Seifert
public class MainActivity extends AppCompatActivity {

    private Button startEndButton;

    private boolean walking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startEndButton = (Button) findViewById(R.id.btnStartEndWalk);

        startEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO start/stop recording time and location
                if(walking){
                    startEndButton.setText(R.string.start);
                    Intent startResults = new Intent(MainActivity.this, ExerciseResultActivity.class);
                    MainActivity.this.startActivity(startResults);
                } else {
                    startEndButton.setText(R.string.end);
                }
                walking = !walking;
            }
        });
    }
}