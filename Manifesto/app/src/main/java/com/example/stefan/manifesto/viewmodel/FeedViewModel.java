package com.example.stefan.manifesto.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.stefan.manifesto.model.Post;
import com.example.stefan.manifesto.repository.PostRepository;
import com.example.stefan.manifesto.utils.SingleLiveEvent;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

import static android.content.ContentValues.TAG;

public class FeedViewModel extends BaseViewModel {

    private PostRepository repository = new PostRepository();
    private MutableLiveData<List<Post>> posts = new MutableLiveData<>();
    private SingleLiveEvent<Boolean> fabRegularPost = new SingleLiveEvent<>();

    public FeedViewModel() {
        getAllPostsForCurrentUserEvents();
    }

    public void getAllPostsForCurrentUserEvents() {
        repository.getAllPostsForCurrentUserEvents(new SingleObserver<List<Post>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(List<Post> list) {
                posts.setValue(list);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage() );
            }
        });
    }

    public void onFabAddRegularPostClick() {
        fabRegularPost.setValue(true);
    }


    public void onFabAddAlertPostClick() {

    }

    public LiveData<List<Post>> getPosts() {
        return posts;
    }

    public LiveData<Boolean> getFabRegularPost() {
        return fabRegularPost;
    }
}
