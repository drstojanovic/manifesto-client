package com.example.stefan.manifesto.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.v4.content.LocalBroadcastManager;

import com.example.stefan.manifesto.model.Event;
import com.example.stefan.manifesto.model.Following;
import com.example.stefan.manifesto.repository.EventRepository;
import com.example.stefan.manifesto.utils.ResponseMessage;
import com.example.stefan.manifesto.utils.SingleLiveEvent;
import com.example.stefan.manifesto.utils.UserSession;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class EventProfileViewModel extends BaseViewModel {

    private MutableLiveData<Event> event = new MutableLiveData<>();
    private SingleLiveEvent<Boolean> resetQueues = new SingleLiveEvent<>();
    private EventRepository repository = new EventRepository();
    private int eventId;

    public EventProfileViewModel(int eventId) {
        super();
        this.eventId = eventId;
        loadEvent();
    }

    public void onFollowButtonClick () {
        if (!UserSession.isUserFollowingEvent(eventId)) {
            repository.addNewFollowing(new Following(0, UserSession.getUser().getId(), event.getValue().getId()),
                    new SingleObserver<ResponseMessage<Following>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(ResponseMessage<Following> followingResponseMessage) {
                            refetchFollowings();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });
        } else {
            repository.removeFollowing(new Following(0, UserSession.getUser().getId(), event.getValue().getId()),
                    new SingleObserver<ResponseMessage<Following>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(ResponseMessage<Following> followingResponseMessage) {
                            refetchFollowings();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });
        }
    }

    private void refetchFollowings() {
        repository.getFollowedEventsOfCurrentUser(new SingleObserver<List<Event>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(List<Event> events) {
                if (events != null && events.size() > 0) {
                    UserSession.setFollowedEvents(events);
                    resetQueues.setValue(true);
                }
                loadEvent();
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    private void loadEvent() {
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

    public LiveData<Boolean> getResetQueues() {
        return resetQueues;
    }
}
