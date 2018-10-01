package com.example.stefan.manifesto.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.databinding.ObservableField;
import android.net.Uri;

import com.example.stefan.manifesto.model.Event;
import com.example.stefan.manifesto.model.NotificationsSettingsItem;
import com.example.stefan.manifesto.model.User;
import com.example.stefan.manifesto.repository.EventRepository;
import com.example.stefan.manifesto.repository.UserRepository;
import com.example.stefan.manifesto.utils.Constants;
import com.example.stefan.manifesto.utils.FirebaseOperations;
import com.example.stefan.manifesto.utils.ResponseMessage;
import com.example.stefan.manifesto.utils.SharedPrefsUtils;
import com.example.stefan.manifesto.utils.SingleLiveEvent;
import com.example.stefan.manifesto.utils.UserSession;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class SettingsViewModel extends BaseViewModel {

    private UserRepository repository = new UserRepository();
    private EventRepository eventRepository = new EventRepository();
    private ObservableField<User> user = new ObservableField<>();
    private ObservableField<String> newImage = new ObservableField<>();
    private boolean imageChanged;
    private SingleLiveEvent<Boolean> imageClick = new SingleLiveEvent<>();
    private SingleLiveEvent<ResponseMessage<User>> savingResponse = new SingleLiveEvent<>();
    private MutableLiveData<Boolean> switchTrackingService = new MutableLiveData<>();
    private MutableLiveData<ArrayList<NotificationsSettingsItem>> settingsItems = new MutableLiveData<>();

    public SettingsViewModel() {
        user.set(UserSession.getUser());
        newImage.set(UserSession.getUser().getImage());
        getFollowedEvents();
    }

    private void getFollowedEvents() {
        ArrayList<NotificationsSettingsItem> list = new ArrayList<>();
        for (Event event : UserSession.getFollowedEvents()) {
            int val = SharedPrefsUtils.getInstance().getIntValue(Constants.NOTIF_SETTINGS_ + event.getId(), 0);
            list.add(new NotificationsSettingsItem(event.getId(), event.getName(), NotificationsSettingsItem.Scope.values()[val]));
        }
        settingsItems.setValue(list);
    }


    public void checkedSettingItem(NotificationsSettingsItem item) {
        SharedPrefsUtils.getInstance().setValue(Constants.NOTIF_SETTINGS_ + item.getEventId(), item.getScope().ordinal());
    }

    //region SavingUserSettings

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
//endregion

    //regionServices

    public void onSwitchTrackingCheckChanged(boolean isSwitchChecked) {
        switchTrackingService.setValue(isSwitchChecked);
    }


    //endregion

    //region GettersAndSetters

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

    public LiveData<Boolean> getSwitchTrackingService() {
        return switchTrackingService;
    }

    public void setSwitchTrackingService(MutableLiveData<Boolean> switchTrackingService) {
        this.switchTrackingService = switchTrackingService;
    }

    public LiveData<ArrayList<NotificationsSettingsItem>> getSettingsItems() {
        return settingsItems;
    }

    public void setSettingsItems(MutableLiveData<ArrayList<NotificationsSettingsItem>> settingsItems) {
        this.settingsItems = settingsItems;
    }

    //endregion
}
