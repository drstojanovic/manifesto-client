package com.example.stefan.manifesto.dao;

import com.example.stefan.manifesto.model.User;
import com.example.stefan.manifesto.utils.ResponseMessage;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LoginDao {

    @POST("user/signup")
    Single<ResponseMessage<User>> registerUser (@Body User user);

    @POST("user/login")
    Single<ResponseMessage<User>> loginUser(@Body User user);

    @GET("following/eventsOfUser/{id}")
    Single<List<Integer>> getFollowedEventsId (@Path("id") int id);

}
