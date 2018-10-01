package com.example.stefan.manifesto.viewmodel;

import android.databinding.ObservableField;
import android.util.Log;

import com.example.stefan.manifesto.model.Post;
import com.example.stefan.manifesto.repository.EventRepository;
import com.example.stefan.manifesto.repository.PostRepository;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

import static android.support.constraint.Constraints.TAG;

public class ShowPostViewModel extends BaseViewModel {

    private PostRepository postRepository = new PostRepository();

    private ObservableField<Post> post = new ObservableField<>();   //ako je klasican post objekat, kad stignu podaci i setuje se taj objekat, nece se prikazati ti podaci

    public ShowPostViewModel() {

    }

    public ShowPostViewModel(int postId) {
        loadPost(postId);
    }

    private void loadPost(int postId) {
        postRepository.getPostById(postId, new SingleObserver<Post>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Post post) {
                ShowPostViewModel.this.post.set(post);
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public ObservableField<Post> getPost() {
        return post;
    }

    public void setPost(ObservableField<Post> post) {
        this.post = post;
    }

}
