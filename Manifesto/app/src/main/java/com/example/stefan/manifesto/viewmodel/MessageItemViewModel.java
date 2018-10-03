package com.example.stefan.manifesto.viewmodel;

import com.example.stefan.manifesto.model.Message;

public class MessageItemViewModel extends BaseViewModel {

    private Message message;

    public MessageItemViewModel(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
