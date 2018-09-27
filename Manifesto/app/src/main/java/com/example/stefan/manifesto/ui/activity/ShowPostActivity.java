package com.example.stefan.manifesto.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.stefan.manifesto.R;
import com.example.stefan.manifesto.databinding.ActivityShowPostBinding;
import com.example.stefan.manifesto.viewmodel.ShowPostViewModel;
import com.example.stefan.manifesto.viewmodel.ShowPostViewModelFactory;

public class ShowPostActivity extends BaseActivity {

    private static final String EXTRA_POST_ID = "EXTRA_POST_ID";
    private ActivityShowPostBinding binding;
    private ShowPostViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_post);
        viewModel = ViewModelProviders.of(this, new ShowPostViewModelFactory(getIntent().getIntExtra(EXTRA_POST_ID, 0)))
                .get(ShowPostViewModel.class);
        binding.setViewModel(viewModel);
    }
}
