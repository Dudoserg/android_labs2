package com.example.lab2.adapter;

public class Car {

    private String mark;
    private String model;
    private int image;

    public Car(String mark, String model, int image){

        this.mark = mark;
        this.model = model;
        this.image = image;
    }

    public String getMark() {
        return this.mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getImage() {
        return this.image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
