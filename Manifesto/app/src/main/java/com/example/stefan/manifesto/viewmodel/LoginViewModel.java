package com.example.stefan.manifesto.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.example.stefan.manifesto.model.User;
import com.example.stefan.manifesto.repository.LoginRepository;
import com.example.stefan.manifesto.utils.ButtonClickLiveData;
import com.example.stefan.manifesto.utils.ResponseMessage;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class LoginViewModel extends BaseViewModel {

    private ObservableField<User> user = new ObservableField<>();
    private MutableLiveData<ResponseMessage<User>> response = new MutableLiveData<>();
    private MutableLiveData<Boolean> signUpButtonClick = new MutableLiveData<>();
    private LoginRepository repository = new LoginRepository();

    public LoginViewModel() {

    }

    public ObservableField<User> getUser() {
        return user;
    }

    public LiveData<Boolean> getSignUpButtonClick() {
        return signUpButtonClick;
    }

    public LiveData<ResponseMessage<User>> getResponse() {
        return response;
    }

    public void onLoginButtonClick() {
        repository.loginUser(user.get(),
                new SingleObserver<ResponseMessage<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(ResponseMessage<User> userResponseMessage) {
                        response.setValue(userResponseMessage);
                    }

                    @Override
                    public void onError(Throwable e) {
                        response.setValue(ResponseMessage.<User>error());
                    }
                });
    }

    public void onSignUpButtonClick() {
        signUpButtonClick.setValue(true);
    }
}
