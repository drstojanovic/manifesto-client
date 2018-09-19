package com.example.stefan.manifesto.viewmodel;

import com.example.stefan.manifesto.model.Event;

public class EventItemViewModel extends BaseViewModel {

    private Event event;

    public EventItemViewModel() {
        event = new Event();
    }

    public EventItemViewModel(Event event) {
        super();
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }
}
