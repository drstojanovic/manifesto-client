package com.example.stefan.manifesto.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;
import android.util.Log;

import com.example.stefan.manifesto.model.User;
import com.example.stefan.manifesto.repository.LoginRepository;
import com.example.stefan.manifesto.utils.ResponseMessage;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

import static android.content.ContentValues.TAG;

public class SignUpViewModel extends ViewModel {

    private ObservableField<User> user = new ObservableField<>();
    private LoginRepository repository = new LoginRepository();
    private MutableLiveData<ResponseMessage<User>> response = new MutableLiveData<>();

    public SignUpViewModel() {
        user.set(new User());
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
                Log.e(TAG, "onError: " + e.getMessage() );
            }
        });
    }

    public LiveData<ResponseMessage<User>> getSignUpResponse() {
        return response;
    }

    public String getName() {
        return user.get().getName();
    }
    public void setName(String name) {
        user.get().setName(name);
    }
    public String getEmail() {
        return  user.get().getEmail();
    }
    public void setEmail(String email) {
        user.get().setEmail(email);
    }
    public String getPassword() {
        return  user.get().getPassword();
    }
    public void setPassword(String password) {
        user.get().setPassword(password);
    }
    public String getCity() {
        return  user.get().getCity();
    }
    public void setCity(String city) {
        user.get().setCity(city);
    }


}
