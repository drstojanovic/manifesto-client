package com.example.stefan.manifesto.viewmodel;

import com.example.stefan.manifesto.model.User;

public class PersonItemViewModel extends BaseViewModel {

    private User user;

    public PersonItemViewModel(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
