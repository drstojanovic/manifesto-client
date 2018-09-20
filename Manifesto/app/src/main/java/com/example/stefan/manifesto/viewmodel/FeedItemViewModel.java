package com.example.stefan.manifesto.viewmodel;

import com.example.stefan.manifesto.model.Post;

public class FeedItemViewModel extends BaseViewModel {

    private Post post;

    public FeedItemViewModel() {
        post = new Post();
    }

    public FeedItemViewModel(Post event) {
        super();
        this.post = event;
    }

    public Post getPost() {
        return post;
    }
}
