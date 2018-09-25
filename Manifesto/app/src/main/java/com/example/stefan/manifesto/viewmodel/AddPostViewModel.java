package com.example.stefan.manifesto.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.example.stefan.manifesto.model.Event;
import com.example.stefan.manifesto.model.Post;
import com.example.stefan.manifesto.repository.EventRepository;
import com.example.stefan.manifesto.repository.PostRepository;
import com.example.stefan.manifesto.utils.DateUtils;
import com.example.stefan.manifesto.utils.ResponseMessage;
import com.example.stefan.manifesto.utils.SingleLiveEvent;
import com.example.stefan.manifesto.utils.UserSession;
import com.google.android.gms.maps.model.LatLng;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class AddPostViewModel extends BaseViewModel {

    private ObservableField<Post> post = new ObservableField<>();
    private MutableLiveData<List<Event>> events = new MutableLiveData<>();
    private MutableLiveData<ResponseMessage<Post>> creationResponse = new MutableLiveData<>();
    private SingleLiveEvent<Boolean> btnAddEscapeRoute = new SingleLiveEvent<>();

    private Event postEvent;
    private EventRepository eventRepository = new EventRepository();
    private PostRepository postRepository = new PostRepository();

    public AddPostViewModel() {
        post.set(new Post());
        postEvent = new Event();
        loadFollowedEvents();
    }

    private void loadFollowedEvents() {
        eventRepository.getFollowedEventsOfCurrentUser(new SingleObserver<List<Event>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(List<Event> list) {
                events.setValue(list);
                postEvent = list.get(0);
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void createPost(LatLng postLocation) {
        postRepository.createPost(extractPost(postLocation), new SingleObserver<ResponseMessage<Post>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(ResponseMessage<Post> postResponseMessage) {
                creationResponse.setValue(postResponseMessage);
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    private Post extractPost(LatLng postLocation) {
        Post p = post.get();
        if (p == null) {
            p = new Post();
            p.setText("");
        }

        p.setEventId(postEvent.getId());
        p.setLatitude(postLocation.latitude);
        p.setLongitude(postLocation.longitude);
        p.setTime(new Date());
        p.setType(Post.REGULAR_TYPE);
        p.setUser(UserSession.getUser());
        return p;
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

    public void onSpinnerEventChooserClick() {

    }


    public void setSelectedEvent(Event event) {
        postEvent = event;
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

    public LiveData<List<Event>> getEvents() {
        return events;
    }

    public LiveData<ResponseMessage<Post>> getCreationResponse() {
        return creationResponse;
    }

}
