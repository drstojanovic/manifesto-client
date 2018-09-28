package com.example.stefan.manifesto.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;

import com.example.stefan.manifesto.R;
import com.example.stefan.manifesto.databinding.ActivityUserProfileBinding;
import com.example.stefan.manifesto.model.Post;
import com.example.stefan.manifesto.ui.adapter.FeedAdapter;
import com.example.stefan.manifesto.viewmodel.UserProfileViewModel;
import com.example.stefan.manifesto.viewmodel.UserProfileViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends BaseActivity implements FeedAdapter.OnPostClickInterface {

    public static final String EXTRA_USER_ID = "EXTRA_USER_ID";

    private UserProfileViewModel viewModel;
    private ActivityUserProfileBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile);
        viewModel = ViewModelProviders.of(this, new UserProfileViewModelFactory(getIntent().getIntExtra(EXTRA_USER_ID, -1))).get(UserProfileViewModel.class);
        binding.setViewModel(viewModel);

        setUpViews();
        setUpObservers();
    }

    private void setUpViews() {
        initToolbar();
        initRecyclerView();
    }

    private void initRecyclerView() {
        binding.recyclerPosts.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerPosts.setHasFixedSize(true);
        binding.recyclerPosts.setAdapter(new FeedAdapter(new ArrayList<Post>(), this));
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle("Profile");
    }

    private void setUpObservers() {
        viewModel.getPosts().observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(@Nullable List<Post> list) {
                ((FeedAdapter) binding.recyclerPosts.getAdapter()).setItems(list);
                binding.btnUserAction.setText(viewModel.isMyProfile() ? getString(R.string.edit) : getString(R.string.message));
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onPostClick(int postId)  {
        Intent intent = new Intent(this, ShowPostActivity.class);
        intent.putExtra(ShowPostActivity.EXTRA_POST_ID, postId);
        startActivity(intent);
    }

}
