package com.example.stefan.manifesto.ui.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stefan.manifesto.R;
import com.example.stefan.manifesto.databinding.FragmentFeedBinding;
import com.example.stefan.manifesto.model.Event;
import com.example.stefan.manifesto.model.Post;
import com.example.stefan.manifesto.ui.adapter.EventAdapter;
import com.example.stefan.manifesto.ui.adapter.FeedAdapter;
import com.example.stefan.manifesto.viewmodel.FeedViewModel;

import java.util.List;


public class FeedFragment extends BaseFragment implements FeedAdapter.OnPostClickInterface {

    private FeedViewModel viewModel;
    private FragmentFeedBinding binding;
    private FeedAdapter adapter;

    public static FeedFragment newInstance() {
        return new FeedFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_feed, container, false);
            viewModel = ViewModelProviders.of(this).get(FeedViewModel.class);
            binding.setViewModel(viewModel);
        }

        if (adapter != null) {
            binding.recyclerFeed.setAdapter(adapter);
        }

        initViews();
        setUpObservers();
        return binding.getRoot();
    }

    private void setUpObservers() {
        viewModel.getPosts().observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(@Nullable List<Post> list) {
                if (list == null) {
                    makeToast(R.string.error);
                }
                setAdapter(list);
            }
        });
    }

    private void initViews() {
        binding.recyclerFeed.setHasFixedSize(true);
        binding.recyclerFeed.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void setAdapter(List<Post> posts) {
        if (adapter == null) {
            adapter = new FeedAdapter(posts, this);
            binding.recyclerFeed.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPostClick(int postId) {

    }
}
