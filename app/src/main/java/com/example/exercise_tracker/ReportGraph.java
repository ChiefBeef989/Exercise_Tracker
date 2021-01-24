package com.example.exercise_tracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.List;

/**
 * Custom view to display a graph of the user's walking speed over time
 */
public class ReportGraph extends View {

    float cWidth, cHeight;

    List<LocationMarker> records;

    //all constructors call init();
    public ReportGraph(Context context) {
        super(context);
        init();
    }

    public ReportGraph(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ReportGraph(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * gets reference of location markers needed to create an exercise report
     */
    public void init(){
        records = ExerciseTracker.getInstance(MainActivity.getInstance()).locationMarkers;
    }

    /**
     * Override onDraw to create a graph
     * @param canvas
     */
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //get canvas dimensions
        cWidth = canvas.getWidth();
        cHeight = canvas.getHeight();

        //declare and initialize Paint objects
        Paint graphPaint = new Paint();
        graphPaint.setColor(Color.BLACK);
        graphPaint.setStyle(Paint.Style.STROKE);
        graphPaint.setStrokeWidth(2f);
        graphPaint.setAntiAlias(true);

        Paint graphDivider = new Paint();
        graphDivider.setColor(R.color.light_gray);
        graphDivider.setStrokeWidth(3f);
        graphDivider.setStyle(Paint.Style.FILL);

        Paint goalPaint = new Paint();
        goalPaint.setColor(Color.GREEN);
        goalPaint.setStrokeWidth(3f);
        goalPaint.setStyle(Paint.Style.FILL);

        Paint textPaint = new Paint();
        textPaint.setTextSize(45f);
        textPaint.setColor(Color.BLACK);

        Paint dot = new Paint();
        dot.setColor(Color.RED);
        dot.setStrokeWidth(5f);
        dot.setStyle(Paint.Style.FILL_AND_STROKE);

        //declare and initialize Path object for the graph's line
        Path graph = new Path();

        //move path to first entry point
        try {
            graph.moveTo(cWidth / records.size(), cHeight - ((cHeight / 11) * ((records.get(1).getDistToLastLocation()) / 5) * 3.6f));
        } catch (IndexOutOfBoundsException e){
            Log.w("WARNING", "Not enough entries");
        }

        //draw lines indicating walking speed from 1-10km/h
        for(int i = 0; i < 11; i++){
            //paint pace goal line green
            if(-(i-11) == MainActivity.getInstance().getPaceGoal())
                canvas.drawLine(0,(cHeight/11)*i,cWidth,(cHeight/11)*i,goalPaint);
            else
                canvas.drawLine(0,(cHeight/11)*i,cWidth,(cHeight/11)*i,graphDivider);

            canvas.drawText(-(i-11) + "km/h",10f,(cHeight/11)*i,textPaint);
        }

        //draw dot and create a line for the graph path
        for(int i = 1; i < records.size(); i++){
            float y = cHeight-((cHeight/11)*records.get(i).getPaceToLastLocation());

            graph.lineTo((cWidth/records.size())*i,y);

            canvas.drawLine((cWidth/records.size())*i,0f,(cWidth/records.size())*i,cHeight,graphDivider);
            canvas.drawCircle((cWidth/records.size())*i,y,10f,dot);
        }
        //draw graph path
        canvas.drawPath(graph,graphPaint);
    }
}
