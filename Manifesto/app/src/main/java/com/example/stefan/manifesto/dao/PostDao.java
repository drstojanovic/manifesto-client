package com.example.stefan.manifesto.dao;

import com.example.stefan.manifesto.model.Post;
import com.example.stefan.manifesto.utils.ResponseMessage;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PostDao {

    @POST("post/forEvents")
    Single<List<Post>> getAllPostsForCurrentUserEvents(@Body List<Integer> eventIds);

    @POST("post/add")
    Single<ResponseMessage<Post>> createPost(@Body Post post);

    @PUT("post/setImageUrl/{id}")
    Single<Integer> setImageUrl(@Path("id") int id, @Body String imageUrl);

    @PUT("post/{id}")
    Single<ResponseMessage<Post>> updatePost(@Path("id") int id, @Body Post post);
}
