package com.example.lab2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.lab2.Lab3.DownloadImageTask;
import com.example.lab2.Lab3.Lab3_Adapter;
import com.example.lab2.Lab3.Car;
import com.example.lab2.Lab3.ServiceDromParcer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Main3Activity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";


    private RecyclerView recyclerView;
    private LinearLayout linearLayout;
    private NestedScrollView nestedScrollView;
    private ProgressBar progressBar;
    private List<Car> cars = new ArrayList<>();
    private static boolean loadmore = true;
    private Lab3_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        initComponents();
        setInitialData();
    }

    private void initComponents() {
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // создаем адаптер
        adapter = new Lab3_Adapter(this, cars);
        adapter.setHasStableIds(true);
        // устанавливаем для списка адаптер
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);


        LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());

        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {
                    //code to fetch more data for endless scrolling
                    if (loadmore) {
                        loadmore = false;
                        method();
                    }
                }
            }
        });

//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                // super.onScrolled(recyclerView, dx, dy);
//                int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
//                if (lastVisiblePosition == recyclerView.getChildCount()) {
//                    if (loadmore) {
//                        loadmore = false;
//                        method();
//                    }
//                }
//            }
//        });
    }

    int counter = 12;

    PendingIntent pendingIntentDromLoad = null;
    final int ServiceDromParserId = 1;
    Intent intentDromLoad = null;

    int carsNumber = 0;

    private void method() {
        new Thread(() -> {
            showProgressView();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Создаем PendingIntent для Service1 Task1
                    pendingIntentDromLoad = createPendingResult(ServiceDromParserId, new Intent(), 0);
                    // Создаем Intent для вызова сервиса, кладем туда данные
                    // и ранее созданный PendingIntent
                    intentDromLoad = new Intent(Main3Activity.this, ServiceDromParcer.class)
                            .putExtra(ServiceDromParcer.PARAM_NUMBER, carsNumber++)
                            .putExtra(ServiceDromParcer.PARAM_PINTENT, pendingIntentDromLoad);
                    // стартуем сервис
                    startService(intentDromLoad);
                }
            });


        }).start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG_TAG, "requestCode = " + requestCode + ", resultCode = " + resultCode);
        switch (requestCode) {
            case ServiceDromParserId: {
                if (resultCode == ServiceDromParcer.STATUS_FINISH) {
                    // Если сервис получаем результат
                    String json = data.getStringExtra(ServiceDromParcer.PARAM_RESULT);
                    Gson gson = new Gson();
                    List<Car> cars = gson.fromJson(json, new TypeToken<List<Car>>() {
                    }.getType());
                    for (Car car : cars) {
                        car.setImage(R.drawable.unknown_car);
//                        AsyncTask<String, Void, Bitmap> execute = new DownloadImageTask(car, adapter)
//                                .execute(car.getImage_x2());
                        adapter.addElement(car);
                    }
                }
                loadmore = true;
                hideProgressView();
                break;
            }
        }
    }

    void showProgressView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    void hideProgressView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }



    private void setInitialData() {
        new Thread(() -> {
            showProgressView();
            runOnUiThread(() -> {
                // Создаем PendingIntent для Service1 Task1
                pendingIntentDromLoad = createPendingResult(ServiceDromParserId, new Intent(), 0);
                // Создаем Intent для вызова сервиса, кладем туда данные
                // и ранее созданный PendingIntent
                intentDromLoad = new Intent(Main3Activity.this, ServiceDromParcer.class)
                        .putExtra(ServiceDromParcer.PARAM_NUMBER, carsNumber++)
                        .putExtra(ServiceDromParcer.PARAM_PINTENT, pendingIntentDromLoad);
                // стартуем сервис
                startService(intentDromLoad);
            });
        }).start();
    }
}