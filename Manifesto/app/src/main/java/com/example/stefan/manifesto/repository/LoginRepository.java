package com.example.stefan.manifesto.repository;

import com.example.stefan.manifesto.dao.ApiManager;
import com.example.stefan.manifesto.model.User;
import com.example.stefan.manifesto.utils.ResponseMessage;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginRepository {

    public void registerUser(User user, SingleObserver<ResponseMessage<User>> singleObserver) {
//       subscribe(ApiManager.getLoginDao().registerUser(user), singleObserver);
       ApiManager.getLoginDao().registerUser(user)
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(singleObserver);
    }

    public void loginUser(User user, SingleObserver<ResponseMessage<User>> singleObserver) {
        ApiManager.getLoginDao().loginUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(singleObserver);
    }

    public void getFollowedEvents(int userId, SingleObserver<List<Integer>> singleObserver) {
        ApiManager.getLoginDao().getFollowedEventsId(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(singleObserver);
    }

}
