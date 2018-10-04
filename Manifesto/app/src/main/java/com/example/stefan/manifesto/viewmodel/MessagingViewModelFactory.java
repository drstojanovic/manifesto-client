package com.example.stefan.manifesto.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.stefan.manifesto.model.User;

public class MessagingViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private int receiverId;

    public MessagingViewModelFactory(int receiverId) {
        this.receiverId = receiverId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MessagingViewModel (receiverId);
    }
}