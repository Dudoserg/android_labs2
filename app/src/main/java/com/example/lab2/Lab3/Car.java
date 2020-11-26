package com.example.lab2.Lab3;


import android.graphics.Bitmap;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Car {

    private int image;
    private Bitmap bitmap = null;
    private String id = "";
    private String href = "";
    private String name = "";
    private String info = "";
    private String price = "";
    private String image_x1 = "";
    private String image_x2 = "";

    public Car(String name, String price, int image){
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public Car() {
    }

    public void setPrice(String price) {
        this.price = price + "₽";
    }
}
