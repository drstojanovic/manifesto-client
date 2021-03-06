package com.example.stefan.manifesto.dao;

import com.example.stefan.manifesto.model.User;
import com.example.stefan.manifesto.model.UserLocation;
import com.example.stefan.manifesto.utils.ResponseMessage;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserDao {

    @POST("user/signup")
    Single<ResponseMessage<User>> registerUser (@Body User user);

    @POST("user/login")
    Single<ResponseMessage<User>> loginUser(@Body User user);

    @GET("user/{id}")
    Single<User> getUser(@Path("id") int id);

    @PUT("user/update")
    Single<ResponseMessage<User>> updateUser(@Body User user);

    @GET("user/all")
    Single<List<User>> getAllUsers();

    @PUT("userLocation/update")
    Single<Void> updateUserLocation(@Body UserLocation userLocation);

    @GET("userLocation/get/{id}")
    Single<UserLocation> getUserLocation(@Path("id") int id);

}
