package com.example.stefan.manifesto.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.stefan.manifesto.model.Event;
import com.example.stefan.manifesto.model.User;
import com.example.stefan.manifesto.repository.EventListRepository;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class EventListViewModel extends BaseViewModel {

    private EventListRepository repository = new EventListRepository();
    private MutableLiveData<List<Event>> events = new MutableLiveData<>();

    public EventListViewModel() {
        getAllEvents();
    }

    public LiveData<List<Event>> getEvents() {
        return events;
    }

    public void getAllEvents() {
        repository.getAllEvents(new SingleObserver<List<Event>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(List<Event> list) {
                events.setValue(list);
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }
}
