package com.example.stefan.manifesto.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.stefan.manifesto.R;
import com.example.stefan.manifesto.databinding.ActivitySignupBinding;
import com.example.stefan.manifesto.model.User;
import com.example.stefan.manifesto.utils.ResponseMessage;
import com.example.stefan.manifesto.viewmodel.LoginViewModel;
import com.example.stefan.manifesto.viewmodel.SignUpViewModel;

public class SignUpActivity extends BaseActivity {

    private SignUpViewModel viewModel;
    private ActivitySignupBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        viewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
        binding.setViewModel(viewModel);

        setUpObservers();
    }

    private void setUpObservers() {
        viewModel.getSignUpResponse().observe(this, new Observer<ResponseMessage<User>>() {
            @Override
            public void onChanged(@Nullable ResponseMessage<User> userResponseMessage) {
                if (userResponseMessage != null) {
                    makeToast(userResponseMessage.getMessage());
                    if (userResponseMessage.isSuccess()) {
                        navigateToActivity(LoginActivity.class);
                    }
                }
            }
        });


    }

}
