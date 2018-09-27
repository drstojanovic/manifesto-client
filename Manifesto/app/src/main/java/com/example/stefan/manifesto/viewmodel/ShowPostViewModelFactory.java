package com.example.stefan.manifesto.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class ShowPostViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private int postId;

    public ShowPostViewModelFactory(int postId) {
        this.postId = postId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ShowPostViewModel (postId);
    }
}