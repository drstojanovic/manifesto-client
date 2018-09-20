package com.example.stefan.manifesto.repository;

import com.example.stefan.manifesto.dao.ApiManager;
import com.example.stefan.manifesto.model.Post;
import com.example.stefan.manifesto.utils.UserSession;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FeedRepository extends BaseRepository {


    public void getAllPostsForCurrentUserEvents(SingleObserver<List<Post>> observer) {
        ApiManager.getFeedDao().getAllPostsForCurrentUserEvents(UserSession.getFollowedEvents())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


}
