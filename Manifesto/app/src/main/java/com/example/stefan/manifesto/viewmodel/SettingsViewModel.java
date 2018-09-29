package com.example.stefan.manifesto.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.databinding.ObservableField;

import com.example.stefan.manifesto.model.User;
import com.example.stefan.manifesto.utils.Constants;
import com.example.stefan.manifesto.utils.FirebaseOperations;
import com.example.stefan.manifesto.utils.SingleLiveEvent;
import com.example.stefan.manifesto.utils.UserSession;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SettingsViewModel extends BaseViewModel {

    private ObservableField<User> user = new ObservableField<>();
    private SingleLiveEvent<Boolean> imageClick = new SingleLiveEvent<>();

    public SettingsViewModel() {
        user.set(UserSession.getUser());
    }

    public void onImageClick(){
        imageClick.setValue(true);
    }


    public void saveSettings() {
    }

    public void saveSelectedImage(Intent data) {
        if (data != null && data.getData() != null) {
            user.get().setImage(data.getData().toString());
            user.notifyChange();
        }
    }

//    private void saveProfileImage(final int postId) {
//        if (imageUris == null || imageUris.size() == 0) return;
//
//        StorageReference ref = FirebaseOperations.getInstance().getStorageReference()
//                .child(Constants.FIREBASE_POSTS)
//                .child(String.valueOf(postId));
//        ref.child("img" + 0).putFile(imageUris.get(0)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                saveImageUrlInDatabase(postId);
//            }
//        });
//    }



    public ObservableField<User> getUser() {
        return user;
    }

    public void setUser(ObservableField<User> user) {
        this.user = user;
    }

    public LiveData<Boolean> getImageClick() {
        return imageClick;
    }

    public void setImageClick(SingleLiveEvent<Boolean> imageClick) {
        this.imageClick = imageClick;
    }

}
