package com.example.stefan.manifesto.dao;

import com.example.stefan.manifesto.model.Post;
import com.example.stefan.manifesto.utils.ResponseMessage;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PostDao {

    @POST("post/forEvents")
    Single<List<Post>> getAllPostsForCurrentUserEvents(@Body List<Integer> eventIds);

    @POST("post/add")
    Single<ResponseMessage<Post>> createPost(@Body Post post);

}
