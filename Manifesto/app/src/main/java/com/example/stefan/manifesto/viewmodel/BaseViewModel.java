package com.example.stefan.manifesto.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.example.stefan.manifesto.repository.BaseRepository;

public abstract class BaseViewModel extends ViewModel {

    private BaseRepository repository;

    @Override
    protected void onCleared() {
        super.onCleared();
    }


}
