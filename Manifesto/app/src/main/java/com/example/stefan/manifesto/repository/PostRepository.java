package com.example.stefan.manifesto.repository;

import com.example.stefan.manifesto.dao.ApiManager;
import com.example.stefan.manifesto.model.Event;
import com.example.stefan.manifesto.model.Post;
import com.example.stefan.manifesto.utils.ResponseMessage;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PostRepository extends BaseRepository {


    public void getAllPostsForCurrentUserEvents(SingleObserver<List<Post>> observer) {
        ArrayList<Integer> arrayList = new ArrayList<>();arrayList.add(1);
        ApiManager.getPostDao().getAllPostsForCurrentUserEvents(arrayList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void createPost(Post post, SingleObserver<ResponseMessage<Post>> observer) {
        ApiManager.getPostDao().createPost(post)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


}
