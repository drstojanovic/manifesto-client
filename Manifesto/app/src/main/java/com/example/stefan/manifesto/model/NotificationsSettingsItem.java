package com.example.stefan.manifesto.model;

public class NotificationsSettingsItem {

    private int eventId;
    private String eventName;
    private Scope scope;

    public NotificationsSettingsItem(int eventId, String eventName, Scope scope) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.scope = scope;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public enum Scope {
        ALL(0, "All notifications"),
        EMERGENCY(1, "Emergency only"),
        EMERGENCY_NEARBY(2, "Nearby emergency"),
        NONE(3, "Notifications off");

        private int key;
        private String value;

        Scope(int key, String value) {
            this.key = key;
            this.value = value;
        }




    }

}
