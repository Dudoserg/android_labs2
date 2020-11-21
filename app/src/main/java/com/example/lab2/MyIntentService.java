package com.example.lab2;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.lab2.Requests.JsonPlaceHolderApi;
import com.example.lab2.Requests.Time;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyIntentService extends IntentService {

    public static String BROADCAST_ID = "BroadcastService3";
    public static String PARAM_TASK = "PARAM_TASK";
    public static String PARAM_STATUS = "PARAM_STATUS";
    public static String PARAM_RESULT = "PARAM_RESULT";

    private static final String GET_IP_ACTION = "com.example.lab2.action.getIp";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.example.lab2.extra.PARAM1";

    public MyIntentService() {
        super("MyIntentService");
    }


    public static void getIp(Context context) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(GET_IP_ACTION);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (GET_IP_ACTION.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);

                try {
                    Time time = connect();
                    sendBroadcast(
                            new Intent(MyIntentService.BROADCAST_ID)
                                    .putExtra(MyIntentService.PARAM_RESULT, new Gson().toJson(time))
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public static Time connect() throws IOException {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://worldtimeapi.org/api/timezone/Asia/Barnaul/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Response<Time> execute = jsonPlaceHolderApi.getTime().execute();

        Time body = execute.body();
        return body;
    }
}