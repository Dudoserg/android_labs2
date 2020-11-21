package com.example.lab2;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Service1 extends Service {

    final String LOG_TAG = "myLogs";


    public final static String PARAM_TIME = "time";
    public final static String PARAM_PINTENT = "pendingIntent";
    public final static String PARAM_RESULT = "result";

    public final static int STATUS_START = 100;
    public final static int STATUS_FINISH = 200;

    public Service1() {
    }

    ExecutorService executorService;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "Service 1 Create");
        executorService = Executors.newFixedThreadPool(2);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "Service 1 Destroy");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int time = intent.getIntExtra(Service1.PARAM_TIME, 1);

        // достаем объект для возврата данных в активити
        PendingIntent pi = intent.getParcelableExtra(Service1.PARAM_PINTENT);
        // запускаем новую задачу
        MyTask myTask = new MyTask(startId, time, pi);
        // кладем ее в пул потоков
        executorService.execute(myTask);

        {
            // сообщаем активити, что задача запустилась
            try {
                pi.send(Service1.this, Service1.STATUS_START, new Intent());
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }

        // восстановления после уничтожения системой и продолжение работы
        return START_REDELIVER_INTENT;
    }


    class MyTask implements Runnable {

        int id;
        int time;
        PendingIntent pendingIntent;

        public MyTask(int id, int time, PendingIntent pendingIntent) {
            this.id = id;
            this.time = time;
            this.pendingIntent = pendingIntent;
            Log.d(LOG_TAG, "MyTask #" + id + " create");
        }


        @Override
        public void run() {
            System.out.println("MyTask #" + id + " start");
            while (time >= 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    Intent intent = new Intent().putExtra(Service1.PARAM_RESULT, time);
                    // Service1.STATUS_FINISH идентификатор возвращаемого значения
                    pendingIntent.send(Service1.this, Service1.STATUS_FINISH, intent);
                    Log.d(LOG_TAG, "send from service #" + id + " to mainActivity: " + time);

                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }

                time--;
            }
            stop();
            Log.d(LOG_TAG, "MyTask#" + id + " end, stopSelfResult(" + id + ") ");

        }

        void stop() {
//            Log.d(LOG_TAG, "MyTask#" + id + " end, stopSelfResult(" + id + ") = " + stopSelfResult(id));
        }
    }
}