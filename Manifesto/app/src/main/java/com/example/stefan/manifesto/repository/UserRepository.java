package com.example.stefan.manifesto.repository;

import com.example.stefan.manifesto.dao.ApiManager;
import com.example.stefan.manifesto.model.User;
import com.example.stefan.manifesto.utils.ResponseMessage;
import com.example.stefan.manifesto.utils.UserSession;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserRepository {

    public void registerUser(User user, SingleObserver<ResponseMessage<User>> singleObserver) {
       ApiManager.getUserDao().registerUser(user)
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(singleObserver);
    }

    public void loginUser(User user, SingleObserver<ResponseMessage<User>> singleObserver) {
        ApiManager.getUserDao().loginUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(singleObserver);
    }

    public void getUser(int id, SingleObserver<User> observer) {
        ApiManager.getUserDao().getUser(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void updateUser(User user, SingleObserver<ResponseMessage<User>> observer) {
        ApiManager.getUserDao().updateUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

}
