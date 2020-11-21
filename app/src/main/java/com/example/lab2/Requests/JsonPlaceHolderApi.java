package com.example.lab2.Requests;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {
    @GET(".")
    Call<Time> getTime();
}
