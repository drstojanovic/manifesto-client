package com.example.stefan.manifesto.viewmodel;

import android.databinding.ObservableField;

import com.example.stefan.manifesto.model.Message;

public class MessageItemViewModel extends BaseViewModel {

    private Message message;
    private ObservableField<Boolean> timeVisibility = new ObservableField<>();

    public MessageItemViewModel(Message message) {
        this.message = message;
        timeVisibility.set(false);
    }

    public Message getMessage() {
        return message;
    }

    public ObservableField<Boolean> getTimeVisibility() {
        return timeVisibility;
    }

    public void setTimeVisibility(ObservableField<Boolean> timeVisibility) {
        this.timeVisibility = timeVisibility;
    }

    public void revertVisibility() {
        timeVisibility.set(!timeVisibility.get());
    }

}
