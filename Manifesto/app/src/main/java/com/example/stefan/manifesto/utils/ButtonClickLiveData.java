package com.example.stefan.manifesto.utils;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

public class ButtonClickLiveData extends MutableLiveData<Boolean> {

    private MutableLiveData<Boolean> mutableLiveData = new MutableLiveData<>();

    public ButtonClickLiveData() {

    }

    public LiveData<Boolean> getLiveData() {
        return this;
    }

}
