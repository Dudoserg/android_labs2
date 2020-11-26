package com.example.lab2.Lab3;

import com.example.lab2.Requests.Time;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Retrofit_Drom {
    @GET(".")
    Call<String> getChaser();
//    @GET()
//    Call<List<ServerResponse>> get(@Url String url);
}
