package com.example.stefan.manifesto.repository;

import com.example.stefan.manifesto.dao.ApiManager;
import com.example.stefan.manifesto.model.Event;
import com.example.stefan.manifesto.model.Post;
import com.example.stefan.manifesto.utils.ResponseMessage;
import com.example.stefan.manifesto.utils.UserSession;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PostRepository extends BaseRepository {

    public void getAllPostsRelevantForUser(SingleObserver<List<Post>> observer) {
        ApiManager.getPostDao().getAllPostsRelevatnForUser(UserSession.getUser().getId())
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


    public void updateImageUrl(int postId, String imageUrl, SingleObserver<Integer> observer) {
        ApiManager.getPostDao().setImageUrl(postId,imageUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    public void updatePost(Post post, SingleObserver<ResponseMessage<Post>> observer) {
        ApiManager.getPostDao().updatePost(post.getId(), post)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    public void getPostById(int id, SingleObserver<Post> observer) {
        ApiManager.getPostDao().getPostById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getPostsOfUser(int userId, SingleObserver<List<Post>> observer) {
        ApiManager.getPostDao().getPostsOfUser(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
