package com.example.make2048;

import android.util.Log;

/**
 * Created by 葉介 on 2015/05/19.
 */
public class TimerManager {
    private long start;
    TimerManager(){
        Log.d("donatu", "start timer");
        start = System.currentTimeMillis();
        Log.d("donatu","started timer");
    }

    public void reStartTime(){
        start = System.currentTimeMillis();
    }

    public float getElipseTime(){
        Log.d("donatu","getting elipse");
        long t = System.currentTimeMillis()-start;
        Log.d("donatu","got elipse");
        t /= 100;
        return (float)(t/10.0);
    }
}
