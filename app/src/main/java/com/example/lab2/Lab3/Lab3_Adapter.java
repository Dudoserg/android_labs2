package com.example.lab2.Lab3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lab2.R;

import java.util.List;

public class Lab3_Adapter extends RecyclerView.Adapter<Lab3_Adapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<Car> cars;

    public Lab3_Adapter(Context context, List<Car> cars) {
        this.cars = cars;
        this.inflater = LayoutInflater.from(context);
    }


    public void addElement(Car car) {
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
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView nameView, model;
        final TextView info;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView2);
            nameView = (TextView) view.findViewById(R.id.name);
            model = (TextView) view.findViewById(R.id.price);
            info = (TextView) view.findViewById(R.id.info);
        }
    }


}
