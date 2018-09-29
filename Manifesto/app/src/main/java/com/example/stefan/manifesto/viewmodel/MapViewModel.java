package com.example.stefan.manifesto.viewmodel;

import android.arch.lifecycle.LiveData;

import com.example.stefan.manifesto.utils.SingleLiveEvent;

public class MapViewModel extends BaseViewModel {

    private SingleLiveEvent<Boolean> resetButton = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> saveButton = new SingleLiveEvent<>();
    private boolean isRootSelectionType;


    public void onResetButtonClick() {
        resetButton.setValue(true);
    }

    public void onSaveButtonClick() {
        saveButton.setValue(true);
    }



    public LiveData<Boolean> getResetButton() {
        return resetButton;
    }

    public LiveData<Boolean> getSaveButton() {
        return saveButton;
    }

    public void setIsRouteSelection(boolean isRootSelectionType) {
        this.isRootSelectionType = isRootSelectionType;
    }

    public boolean isRootSelectionType() {
        return isRootSelectionType;
    }
}
