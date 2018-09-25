package com.example.stefan.manifesto.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    private static final String BASE_URL = "http://10.14.116.218:8080/";
    private static LoginDao loginDao;
    private static EventDao eventDao;
    private static PostDao postDao;
    private static OkHttpClient client;

    private static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create();

    private static Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
            .client(getClient())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();


    private static OkHttpClient getClient() {
        if (client == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        }
        return client;
    }

    public static LoginDao getLoginDao() {
        if (loginDao == null) {
            loginDao = retrofit.create(LoginDao.class);
        }
        return loginDao;
    }

    public static EventDao getEventDao() {
        if (eventDao == null) {
            eventDao = retrofit.create(EventDao.class);
        }
        return eventDao;
    }

    public static PostDao getPostDao() {
        if (postDao == null) {
            postDao = retrofit.create(PostDao.class);
        }
        return postDao;
    }
}
