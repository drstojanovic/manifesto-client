package com.example.stefan.manifesto.viewmodel;

import com.example.stefan.manifesto.model.Post;

public class FeedItemViewModel extends BaseViewModel {

    private Post post;

    public FeedItemViewModel() {
        post = new Post();
    }

    public FeedItemViewModel(Post post) {
        super();
        this.post = post;
    }

    public Post getPost() {
        return post;
    }

    public boolean isEmergency() {
        return post != null && post.getType() != null && post.getType().equals("emergency");
    }
}
