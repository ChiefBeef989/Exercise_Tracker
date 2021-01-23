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

public class ReportGraph extends View {

    float cWidth, cHeight;

    List<LocationMarker> records;

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

    public void init(){
        records = ExerciseTracker.getInstance(MainActivity.getInstance()).locationMarkers;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        cWidth = canvas.getWidth();
        cHeight = canvas.getHeight();

        Log.i("HEIGHT", cHeight+"");

        Paint border = new Paint();
        border.setColor(Color.BLACK);
        border.setStyle(Paint.Style.STROKE);
        border.setStrokeWidth(2f);

        Paint graphDivider = new Paint();
        graphDivider.setColor(R.color.light_gray);

        Paint textPaint = new Paint();
        textPaint.setTextSize(30f);
        textPaint.setColor(Color.BLACK);

        Paint dot = new Paint();
        dot.setColor(Color.RED);
        dot.setStrokeWidth(5f);
        dot.setStyle(Paint.Style.FILL_AND_STROKE);

        Path graph = new Path();

        graph.moveTo(cWidth/records.size(),cHeight-((cHeight/11)*((records.get(1).getDistToLastLocation())/5)*3.6f));

        canvas.drawRect(0f, 0f, cWidth, cHeight, border);

        for(int i = 0; i < 11; i++){
            canvas.drawLine(0,(cHeight/11)*i,cWidth,(cHeight/11)*i,graphDivider);
            canvas.drawText(-(i-11) + "km/h",2f,(cHeight/11)*i,textPaint);
        }

        for(int i = 1; i < records.size(); i++){
            float speed = ((records.get(i).getDistToLastLocation())/5)*3.6f;
            Log.i("SPEED ", speed+"km/h");
            float y = cHeight-((cHeight/11)*speed);
            graph.lineTo((cWidth/records.size())*i,y);
            canvas.drawLine((cWidth/records.size())*i,0f,(cWidth/records.size())*i,cHeight,graphDivider);
            canvas.drawCircle((cWidth/records.size())*i,y,10f,dot);
        }

        canvas.drawPath(graph,border);
    }
}
