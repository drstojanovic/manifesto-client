package com.example.stefan.manifesto.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.stefan.manifesto.model.User;
import com.example.stefan.manifesto.repository.UserRepository;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

import static android.support.constraint.Constraints.TAG;

public class PeopleViewModel extends BaseViewModel {

    private UserRepository userRepository = new UserRepository();
    private MutableLiveData<List<User>> people = new MutableLiveData<>();

    public PeopleViewModel() {
        loadUsers();
    }

    private void loadUsers() {
        userRepository.getAllUsers(new SingleObserver<List<User>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(List<User> users) {
                people.setValue(users);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "Error during loading users PeopleViewModel");
            }
        });
    }

    public LiveData<List<User>> getPeople() {
        return people;
    }
}
