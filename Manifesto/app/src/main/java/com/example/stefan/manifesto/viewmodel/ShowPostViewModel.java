package com.example.stefan.manifesto.viewmodel;

import com.example.stefan.manifesto.model.Post;
import com.example.stefan.manifesto.repository.PostRepository;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class ShowPostViewModel extends BaseViewModel {

    private Post post;
    private PostRepository repository = new PostRepository();

    public ShowPostViewModel() {

    }

    public ShowPostViewModel(int postId) {
        loadPost(postId);
    }

    private void loadPost(int postId) {
        repository.getPostById(postId, new SingleObserver<Post>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Post post) {
                ShowPostViewModel.this.post = post;
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
