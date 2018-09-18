package com.example.stefan.manifesto.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

import com.example.stefan.manifesto.model.User;
import com.example.stefan.manifesto.repository.LoginRepository;
import com.example.stefan.manifesto.utils.ResponseMessage;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class SignUpViewModel extends ViewModel {

    private ObservableField<User> user = new ObservableField<>();
    private LoginRepository repository = new LoginRepository();
    private MutableLiveData<ResponseMessage<User>> response = new MutableLiveData<>();

    public SignUpViewModel() {

    }

    public ObservableField<User> getUser() {
        return user;
    }

    public void onSignUpButtonClick() {
        repository.registerUser(user.get(), new SingleObserver<ResponseMessage<User>>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(ResponseMessage<User> responseMessage) {
                response.setValue(responseMessage);
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public LiveData<ResponseMessage<User>> getSignUpResponse() {
        return response;
    }
}
