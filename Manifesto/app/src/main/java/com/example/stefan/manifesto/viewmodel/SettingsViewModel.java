package com.example.stefan.manifesto.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.databinding.ObservableField;
import android.net.Uri;

import com.example.stefan.manifesto.model.User;
import com.example.stefan.manifesto.repository.UserRepository;
import com.example.stefan.manifesto.utils.Constants;
import com.example.stefan.manifesto.utils.FirebaseOperations;
import com.example.stefan.manifesto.utils.ResponseMessage;
import com.example.stefan.manifesto.utils.SingleLiveEvent;
import com.example.stefan.manifesto.utils.UserSession;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class SettingsViewModel extends BaseViewModel {

    private UserRepository repository = new UserRepository();
    private ObservableField<User> user = new ObservableField<>();
    private ObservableField<String> newImage = new ObservableField<>();
    private boolean imageChanged;
    private SingleLiveEvent<Boolean> imageClick = new SingleLiveEvent<>();
    private SingleLiveEvent<ResponseMessage<User>> savingResponse = new SingleLiveEvent<>();

    public SettingsViewModel() {
        user.set(UserSession.getUser());
        newImage.set(UserSession.getUser().getImage());
    }

    public void onImageClick() {
        imageClick.setValue(true);
    }

    public void saveChanges() {
        if (!imageChanged) {
            saveSettingsNoImage();
            return;
        }

        FirebaseOperations.getInstance().getStorageReference()
                .child(Constants.FIREBASE_USERS)
                .child(String.valueOf(UserSession.getUser().getId()))
                .child("profile").putFile(Uri.parse(newImage.get())).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                saveSettings();
            }
        });
    }

    private void saveSettingsNoImage() {
        repository.updateUser(user.get(), new SingleObserver<ResponseMessage<User>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(ResponseMessage<User> userResponseMessage) {
                UserSession.setUser(userResponseMessage.getResponseBody());
                savingResponse.setValue(userResponseMessage);
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    private void saveSettings() {
        FirebaseOperations.getInstance().getStorageReference()
                .child(Constants.FIREBASE_USERS)
                .child(String.valueOf(UserSession.getUser().getId()))
                .child("profile")
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        user.get().setImage(uri.toString());
                        saveSettingsNoImage();
                    }
                });
    }

    public void setSelectedImage(Intent data) {
        if (data != null && data.getData() != null) {
            newImage.set(data.getData().toString());
            imageChanged = true;
        }
    }

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

    public ObservableField<String> getNewImage() {
        return newImage;
    }

    public void setNewImage(ObservableField<String> newImage) {
        this.newImage = newImage;
    }

    public SingleLiveEvent<ResponseMessage<User>> getSavingResponse() {
        return savingResponse;
    }

    public void setSavingResponse(SingleLiveEvent<ResponseMessage<User>> savingResponse) {
        this.savingResponse = savingResponse;
    }
}
