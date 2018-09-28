package com.example.stefan.manifesto.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class UserProfileViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private int userId;

    public UserProfileViewModelFactory(int userId) {
        this.userId = userId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new UserProfileViewModel (userId);
    }
}