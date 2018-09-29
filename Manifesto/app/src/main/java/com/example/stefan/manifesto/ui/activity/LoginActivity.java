package com.example.stefan.manifesto.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.stefan.manifesto.R;
import com.example.stefan.manifesto.databinding.ActivityLoginBinding;
import com.example.stefan.manifesto.model.User;
import com.example.stefan.manifesto.utils.ResponseMessage;
import com.example.stefan.manifesto.utils.UserSession;
import com.example.stefan.manifesto.viewmodel.LoginViewModel;


public class LoginActivity extends BaseActivity {

    private LoginViewModel viewModel;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        binding.setViewModel(viewModel);

        setUpObservers();
    }

    private void setUpObservers() {
        viewModel.getResponse().observe(this, new Observer<ResponseMessage<User>>() {
            @Override
            public void onChanged(@Nullable ResponseMessage<User> userResponseMessage) {
                if (userResponseMessage == null)
                    return;

                if (userResponseMessage.isSuccess()) {
                    UserSession.setUser(userResponseMessage.getResponseBody());
                    navigateToActivity(MainActivity.class);
                    finish();
                } else {
                    makeToast(userResponseMessage.getMessage());
                }
            }
        });

        viewModel.getSignUpButtonClick().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                navigateToActivity(SignUpActivity.class);
            }
        });

    }
}
