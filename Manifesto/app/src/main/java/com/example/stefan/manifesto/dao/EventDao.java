package com.example.stefan.manifesto.dao;

import com.example.stefan.manifesto.model.Event;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface EventDao {

    @GET("event/all")
    Single<List<Event>> getAllEvents();

    @GET("event/{id}")
    Single<Event> getEventById(@Path("id") int id);

    @GET("event/userId/{id}")
    Single<List<Event>> getAllEventsOfUser(@Path("id") int id);

}
