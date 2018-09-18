package com.example.stefan.manifesto.dao;

import com.example.stefan.manifesto.model.User;
import com.example.stefan.manifesto.utils.ResponseMessage;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginDao {

    @POST("user/signup")
    Single<ResponseMessage<User>> registerUser (@Body User user);

    @POST("user/login")
    Single<ResponseMessage<User>> loginUser(@Body User user);

}
