package com.example.lab2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";

    // поле вывода результатов из сервиса 1
    TextView textView_service1Result;
    // кнопка старта задачи в сервисе 1
    Button button_service1Start;
    // кнопка остановки сервиса 1
    Button button_service1Stop;

    // объект для возврата из сервиса данных
    PendingIntent pi_1;
    Intent intent_1;
    final int ServiceId_1 = 1;


    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    TextView textView_service2Result;
    Button button_service2Start;
    Button button_service2DownInterval;
    Button button_service2UpInterval;
    private Intent intent_2;
    private ServiceConnection s2Conn;
    private Service2 service2;
    private boolean bound;
    private long interval;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponent();
        start();
    }

    private void initComponent() {
        // Получаем все элементы UI
        this.textView_service1Result = (TextView) this.findViewById(R.id.textView_service1Result);
        this.button_service1Start = (Button) this.findViewById(R.id.button_service1Start);
        this.button_service1Stop = (Button) this.findViewById(R.id.button_service1Stop);

        this.button_service1Start.setOnClickListener(view -> {
            Log.d(LOG_TAG, "btn click");
            // Создаем PendingIntent для Service1 Task1
            pi_1 = createPendingResult(ServiceId_1, new Intent(), 0);
            // Создаем Intent для вызова сервиса, кладем туда данные
            // и ранее созданный PendingIntent
            intent_1 = new Intent(this, Service1.class)
                    .putExtra(Service1.PARAM_TIME, 7)
                    .putExtra(Service1.PARAM_PINTENT, pi_1);
            // стартуем сервис
            startService(intent_1);
        });

        this.button_service1Stop.setOnClickListener(view -> {
            // останавливаем сервис
            stopService(intent_1);
        });

        ///////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////
        this.textView_service2Result = (TextView) this.findViewById(R.id.textView_service2Result);
        this.button_service2Start = (Button) this.findViewById(R.id.button_service2Start);
        this.button_service2DownInterval = (Button) this.findViewById(R.id.button_service2DownInterval);
        this.button_service2UpInterval = (Button) this.findViewById(R.id.button_service2UpInterval);

        intent_2 = new Intent(this, Service2.class);
        s2Conn = new ServiceConnection() {

            public void onServiceConnected(ComponentName name, IBinder binder) {
                Log.d(LOG_TAG, "MainActivity onServiceConnected");
                service2 = ((Service2.MyBinder) binder).getService();
                bound = true;
            }

            public void onServiceDisconnected(ComponentName name) {
                Log.d(LOG_TAG, "MainActivity onServiceDisconnected");
                bound = false;
            }
        };

        this.button_service2Start.setOnClickListener(view -> {
            onClickStart(view);
        });
        this.button_service2UpInterval.setOnClickListener(view -> {
            changeInterval(100);
        });

        this.button_service2DownInterval.setOnClickListener(view -> {
            changeInterval(-100);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(intent_2, s2Conn, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!bound) return;
        unbindService(s2Conn);
        bound = false;
    }

    public void onClickStart(View v) {
        startService(intent_2);

        new Thread(() -> {
            while (!bound) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            runOnUiThread(() -> {
                changeInterval(0);
            });
        }).start();
    }

    long changeInterval(int tmp) {
        if (!bound) return 0;
        interval = service2.changeInterval(tmp);
        textView_service2Result.setText("interval = " + interval);
        return interval;
    }


    /**
     * обработчик возвращаемых значений из сервиса
     *
     * @param requestCode идентификатор сервиса
     * @param resultCode  идентификатор возвращаемого значения
     * @param data        возвращаемое значение
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(LOG_TAG, "requestCode = " + requestCode + ", resultCode = " + resultCode);


        switch (requestCode) {
            case ServiceId_1: {
                // Ловим сообщения о старте service #1
                if (resultCode == Service1.STATUS_START) {
                    // Если сервис стартанул, напишем об этом
                    textView_service1Result.setText("Task1 start");
                } else
                    // Ловим сообщения об окончании задач from service #1
                    if (resultCode == Service1.STATUS_FINISH) {
                        int result = data.getIntExtra(Service1.PARAM_RESULT, 0);
                        textView_service1Result.setText("" + result);
                    }

                break;
            }
        }

    }


    private void start() {

    }


}