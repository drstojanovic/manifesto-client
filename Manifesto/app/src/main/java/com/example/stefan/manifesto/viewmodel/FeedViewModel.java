package com.example.stefan.manifesto.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.stefan.manifesto.model.Event;
import com.example.stefan.manifesto.model.Post;
import com.example.stefan.manifesto.repository.EventRepository;
import com.example.stefan.manifesto.repository.FeedRepository;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

import static android.content.ContentValues.TAG;

public class FeedViewModel extends BaseViewModel {

    private FeedRepository repository = new FeedRepository();
    private MutableLiveData<List<Post>> posts = new MutableLiveData<>();

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

    public void onFloatingButtonClick() {

    }

    public LiveData<List<Post>> getPosts() {
        return posts;
    }
}
