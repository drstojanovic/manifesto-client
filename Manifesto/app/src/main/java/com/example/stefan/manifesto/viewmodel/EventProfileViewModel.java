package com.example.stefan.manifesto.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.stefan.manifesto.model.Event;
import com.example.stefan.manifesto.repository.EventRepository;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class EventProfileViewModel extends BaseViewModel {

    private MutableLiveData<Event> event = new MutableLiveData<>();
    private EventRepository repository = new EventRepository();

    public EventProfileViewModel(int eventId) {
        super();
        loadEvent(eventId);
    }

    public void onFollowButtonClick () {

    }

    private void loadEvent(int eventId) {
        repository.getEventById(eventId,
                new SingleObserver<Event>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Event e) {
                        event.setValue(e);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public LiveData<Event> getEvent() {
        return event;
    }
}
