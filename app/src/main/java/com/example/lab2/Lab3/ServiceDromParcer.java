package com.example.lab2.Lab3;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.lab2.Requests.JsonPlaceHolderApi;
import com.example.lab2.Requests.Time;
import com.example.lab2.Service1;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ServiceDromParcer extends Service {

    public final static String PARAM_NUMBER = "number";
    public final static String PARAM_PINTENT = "pendingIntent";
    public final static String PARAM_RESULT = "result";

    public final static int STATUS_START = 100;
    public final static int STATUS_FINISH = 200;

    public ServiceDromParcer() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int number = intent.getIntExtra(PARAM_NUMBER, 0);
        // достаем объект для возврата данных в активити
        PendingIntent pi = intent.getParcelableExtra(PARAM_PINTENT);

        new Thread(() -> {
            // Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();
            String url = "https://novosibirsk.drom.ru/toyota/chaser/page" + ((number / 2) + 1) + "/";
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();

            Retrofit_Drom drom_interface_request = retrofit.create(Retrofit_Drom.class);

            Response<String> execute = null;
            try {
                execute = drom_interface_request.getChaser().execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String html = execute.body();
            DromCarsParser dromCarsParser = new DromCarsParser();
            List<Car> cars_all = new ArrayList<>();
            try {
                cars_all = dromCarsParser.getCars(html);
            } catch (IOException e) {
                e.printStackTrace();
            }

            List<Car> car_result = new ArrayList<>();
            int start = (number % 2) * 10;
            int end = ((number % 2) + 1) * 10;
            for (int i = start; i < end; i++) {
                car_result.add(cars_all.get(i));
            }

            Gson gson = new Gson();
            String json = gson.toJson(car_result);

            // сообщаем активити, что задача запустилась
            try {
                Intent intent1 = new Intent().putExtra(ServiceDromParcer.PARAM_RESULT, json);
                // Service1.STATUS_FINISH идентификатор возвращаемого значения
                pi.send(ServiceDromParcer.this, Service1.STATUS_FINISH, intent1);
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }

        }).start();

        return START_REDELIVER_INTENT;
    }
}