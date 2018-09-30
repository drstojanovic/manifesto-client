package com.example.stefan.manifesto.dao;

import com.example.stefan.manifesto.model.Event;
import com.example.stefan.manifesto.model.Following;
import com.example.stefan.manifesto.utils.ResponseMessage;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EventDao {

    @GET("event/all")
    Single<List<Event>> getAllEvents();

    @GET("event/{id}")
    Single<Event> getEventById(@Path("id") int id);

    @GET("event/userId/{id}")
    Single<List<Event>> getAllEventsOfUser(@Path("id") int id);

    @GET("event/getName/{id}")
    Single<String> getEventName(@Path("id") int id);

    @GET("following/eventsOfUser/{id}")
    Single<List<Integer>> getFollowedEventsIds(@Path("id") int id);

    @POST("following/add")
    Single<ResponseMessage<Following>> addNewFollowing(@Body Following following);

    @POST("following/remove")
    Single<ResponseMessage<Following>> removeFollowing(@Body Following following);
}
