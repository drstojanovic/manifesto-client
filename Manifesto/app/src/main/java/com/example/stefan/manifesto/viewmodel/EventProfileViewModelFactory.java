package com.example.stefan.manifesto.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;


public class EventProfileViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private int eventId;

    public EventProfileViewModelFactory(int eventId) {
        this.eventId = eventId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new EventProfileViewModel(eventId);
    }
}