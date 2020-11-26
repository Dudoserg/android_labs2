package com.example.lab2.Lab3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.lab2.R;

import java.util.List;

public class Lab3_Adapter extends RecyclerView.Adapter<Lab3_Adapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<Car> cars;
    private Context context;

    public Lab3_Adapter(Context context, List<Car> cars) {
        this.cars = cars;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void update() {
        notifyDataSetChanged();

    }


    public void addElement(Car car) {
        this.cars.add(car);
        notifyDataSetChanged();
    }

    public void addElement(Car car, Lab3_Adapter adapter) {
        AsyncTask<String, Void, Bitmap> execute = new DownloadImageTask(car, adapter)
                .execute(car.getImage_x2());
        this.cars.add(car);
        notifyDataSetChanged();
    }


    @Override
    public Lab3_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Lab3_Adapter.ViewHolder holder, int position) {
        Car car = cars.get(position);
        if (car.getBitmap() == null)
            holder.imageView.setImageResource(R.drawable.unknown_car);
        else
            holder.imageView.setImageBitmap(car.getBitmap());
        holder.nameView.setText(car.getName());
        holder.model.setText(car.getPrice());
        holder.info.setText(car.getInfo());

        holder.progressBar_loadImage.setVisibility(View.VISIBLE);
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ee) {
                ee.printStackTrace();
            }

            Glide.with(context)
                    .load(car.getImage_x2())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                            holder.progressBar_loadImage.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ee) {
                                ee.printStackTrace();
                            }
                            holder.progressBar_loadImage.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.imageView);
        }).start();

    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView nameView, model;
        final TextView info;
        final ProgressBar progressBar_loadImage;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView2);
            nameView = (TextView) view.findViewById(R.id.name);
            model = (TextView) view.findViewById(R.id.price);
            info = (TextView) view.findViewById(R.id.info);
            progressBar_loadImage = (ProgressBar) view.findViewById(R.id.progressBar2);
        }

        void showProgressView() {
            progressBar_loadImage.setVisibility(View.VISIBLE);
        }

        void hideProgressView() {
            progressBar_loadImage.setVisibility(View.INVISIBLE);
        }

    }
}
