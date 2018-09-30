package com.example.stefan.manifesto.utils;

import com.example.stefan.manifesto.dao.ApiManager;
import com.example.stefan.manifesto.model.User;
import com.example.stefan.manifesto.repository.EventRepository;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UserSession {

    private static User user;
    private static List<Integer> followedEvents;

    public static User getUser() {
        return user;
    }

    public static void setUser (User newUser) {
        user = newUser;
    }

    public static void setFollowedEvents(List<Integer> followedEvents) {
        UserSession.followedEvents = followedEvents;
    }

    public static List<Integer> getFollowedEvents() {
        return followedEvents;
    }

    public static boolean isUserFollowingEvent(int eventId) {
        for (Integer followedEvent : followedEvents) {
            if (followedEvent.equals(eventId)) {
                return true;
            }
        }
        return false;
    }

//    public static void fetchFollowings() {
//        ApiManager.getEventDao().getFollowedEventsIds(UserSession.getUser().getId())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new SingleObserver<List<Integer>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onSuccess(List<Integer> integers) {
//                        setFollowedEvents(integers);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//                });
//    }


}
