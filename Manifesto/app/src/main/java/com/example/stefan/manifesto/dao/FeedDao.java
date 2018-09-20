package com.example.stefan.manifesto.dao;

import com.example.stefan.manifesto.model.Post;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FeedDao {

    @POST("post/forEvents")
    Single<List<Post>> getAllPostsForCurrentUserEvents(@Body List<Integer> eventIds);

}
