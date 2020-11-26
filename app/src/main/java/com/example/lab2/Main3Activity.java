package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.lab2.adapter.Lab3_Adapter;
import com.example.lab2.adapter.Car;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Main3Activity extends AppCompatActivity {

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

        setInitialData();
        initComponents();
    }

    private void initComponents() {
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        this.linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
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

    private void method() {
        new Thread(() -> {
            showProgressView();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 5; i++) {
                        adapter.addElement(
                                new Car("kek #" + (counter++), UUID.randomUUID().toString(), R.drawable.cat)
                        );
                    }
                    loadmore = true;
                }
            });
            hideProgressView();

        }).start();
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

        cars.add(new Car("Huawei P10", "Huawei", R.drawable.bear_pixel_10));
        cars.add(new Car("Elite z3", "HP", R.drawable.bear_pixel_10));
        cars.add(new Car("Galaxy S8", "Samsung", R.drawable.cat));
        cars.add(new Car("LG G 5", "LG", R.drawable.cat));
        cars.add(new Car("kek #1", "LG", R.drawable.cat));
        cars.add(new Car("kek #2", "LG", R.drawable.cat));
        cars.add(new Car("kek #3", "LG", R.drawable.cat));
        cars.add(new Car("kek #4", "LG", R.drawable.cat));
        cars.add(new Car("kek #5", "LG", R.drawable.cat));
    }
}