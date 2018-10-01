package com.example.stefan.manifesto.viewmodel;

import com.example.stefan.manifesto.model.NotificationsSettingsItem;

public class NotificationSettingsItemViewModel extends BaseViewModel {

    private NotificationsSettingsItem settingsItem;

    public NotificationSettingsItemViewModel(NotificationsSettingsItem item) {
        settingsItem = item;
    }


    public NotificationsSettingsItem getSettingsItem() {
        return settingsItem;
    }

    public void setSettingsItem(NotificationsSettingsItem settingsItem) {
        this.settingsItem = settingsItem;
    }
}
