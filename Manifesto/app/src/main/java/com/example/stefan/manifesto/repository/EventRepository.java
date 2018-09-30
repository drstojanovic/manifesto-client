package com.example.stefan.manifesto.repository;

import com.example.stefan.manifesto.dao.ApiManager;
import com.example.stefan.manifesto.model.Event;
import com.example.stefan.manifesto.utils.HelperUtils;
import com.example.stefan.manifesto.utils.UserSession;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

public class EventRepository extends BaseRepository {

    public void getAllEvents(SingleObserver<List<Event>> observer) {
        ApiManager.getEventDao().getAllEvents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getEventById(int id, SingleObserver<Event> observer) {
        ApiManager.getEventDao().getEventById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getFollowedEventsOfCurrentUser(SingleObserver<List<Event>> observer) {
        ApiManager.getEventDao().getAllEventsOfUser(UserSession.getUser().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getEventName(int eventId, SingleObserver<String> observer) {
        ApiManager.getEventDao().getEventName(eventId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getFollowedEventsIds(SingleObserver<List<Integer>> observer) {
        ApiManager.getEventDao().getFollowedEventsIds(UserSession.getUser().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

//    public void getAllEventsWithFollowingFlag(SingleObserver<List<Event>> observer) {
//        Single.zip(ApiManager.getEventDao().getAllEvents(), ApiManager.getEventDao().getFollowedEventsIds(UserSession.getUser().getId())
//                , new BiFunction<List<Event>, List<Integer>, List<Event>>() {
//                    @Override
//                    public List<Event> apply(List<Event> events, List<Integer> integers) throws Exception {
//                        for (Event event : events) {
//                            if (HelperUtils.isInList(integers, event.getId())) {
//                                event.setFollowedByCurrentUser(true);
//                            }
//                        }
//                        return events;
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer);
//    }


}
