package com.example.make2048;

import android.util.Log;

/**
 * Created by 葉介 on 2015/05/19.
 */
public class TimerManager implements alias{
    private long start;
    private long timelimit;

    TimerManager(){
        Log.d("donatu", "start timer");
        start = System.currentTimeMillis();
        timelimit = TIME_LIMIT;
    }

    public void reStartTime(){
        start = System.currentTimeMillis();
    }

    public float getElipseTime(){
        long t = System.currentTimeMillis()-start;
        t /= 100;
        return (float)(timelimit-(t/10.0));
    }
}
