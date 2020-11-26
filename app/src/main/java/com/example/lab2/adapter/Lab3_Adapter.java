package com.example.lab2.adapter;

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


    public void addElement(Car car){
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
        holder.imageView.setImageResource(car.getImage());
        holder.nameView.setText(car.getMark());
        holder.companyView.setText(car.getModel());
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView nameView, companyView;
        public ViewHolder(View view){
            super(view);
            imageView = (ImageView)view.findViewById(R.id.image);
            nameView = (TextView) view.findViewById(R.id.name);
            companyView = (TextView) view.findViewById(R.id.company);
        }
    }


}
