package com.example.rdshv40;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServerHelp {
    ServerApi service = null;
    OkHttpClient client = null;

    public ServerHelp()
    {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(1, TimeUnit.MINUTES);
        builder.writeTimeout(1, TimeUnit.MINUTES);
        builder.readTimeout(1, TimeUnit.MINUTES);
        client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://rdshtest.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        service = retrofit.create(ServerApi.class);
    }

    public ServerApi getService ()
    {
        return service;
    }
}
