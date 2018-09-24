package com.example.stefan.manifesto.viewmodel;

import android.arch.lifecycle.LiveData;
import android.databinding.ObservableField;

import com.example.stefan.manifesto.model.Post;
import com.example.stefan.manifesto.utils.SingleLiveEvent;

public class AddPostViewModel extends BaseViewModel {

    private ObservableField<Post> post = new ObservableField<>();
    private SingleLiveEvent<Boolean> btnAddEscapeRoute = new SingleLiveEvent<>();

    public AddPostViewModel() {

    }

    public void onTakePictureButtonClick() {

    }

    public void onSelectPictureButtonClick() {

    }

    public void onAddEscapeRouteButtonClick() {
        btnAddEscapeRoute.setValue(true);
    }

    public void onCreatePostButtonClick() {

    }


    public ObservableField<Post> getPost() {
        return post;
    }

    public String getText() {
        return post.get().getText();
    }

    public void setText(String text) {
        post.get().setText(text);
    }

    public LiveData<Boolean> getBtnAddEscapeRoute() {
        return btnAddEscapeRoute;
    }
}
