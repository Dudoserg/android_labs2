package com.example.lab2;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * local binding
 */
public class Service2 extends Service {

    final String LOG_TAG = "myLogs";

    MyBinder binder = new MyBinder();

    Timer timer;
    TimerTask task;
    long interval = 1000;
    long iter = 0L;

    public Service2() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "Service 2 onCreate");
        timer = new Timer();
        schedule();
    }

    void schedule() {
        if (task != null)
            task.cancel();
        if (interval > 0) {
            task = new TimerTask() {
                public void run() {
                    Log.d(LOG_TAG, "================== Service 2 some task iteration # " + (iter++) + "=====================");
                }
            };
            timer.schedule(task, 1000, interval);
        }
    }

    long changeInterval(long gap) {
        interval = interval + gap;
        if (interval < 0) interval = 0;
        schedule();
        return interval;
    }

    class MyBinder extends Binder {
        Service2 getService() {
            return Service2.this;
        }
    }
}