package com.example.lab2.Lab3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    final String LOG_TAG = "myLogs";


    private Car car;
    private Lab3_Adapter lab3_adapter;

    public DownloadImageTask(Car car, Lab3_Adapter adapter) {
        this.car = car;
        this.lab3_adapter = adapter;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
            e.printStackTrace();
        }
        car.setBitmap(mIcon11);
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        this.lab3_adapter.update();
    }
}