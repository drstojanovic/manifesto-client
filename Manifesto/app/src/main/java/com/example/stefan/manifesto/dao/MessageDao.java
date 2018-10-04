package com.example.stefan.manifesto.dao;

import com.example.stefan.manifesto.model.Message;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MessageDao {

    @POST("message/add")
    Single<Boolean> addMessage(@Body Message message);

    @GET("message/all/interlocutorOne/{oid}/interlocutorTwo/{tid}")
    Single<List<Message>> getChat(@Path("oid") int senderId, @Path("tid") int receiverId);
}
