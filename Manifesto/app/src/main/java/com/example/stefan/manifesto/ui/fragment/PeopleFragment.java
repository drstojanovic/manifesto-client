package com.example.stefan.manifesto.ui.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stefan.manifesto.R;
import com.example.stefan.manifesto.databinding.FragmentPeopleBinding;
import com.example.stefan.manifesto.model.User;
import com.example.stefan.manifesto.ui.activity.MessagingActivity;
import com.example.stefan.manifesto.ui.activity.UserProfileActivity;
import com.example.stefan.manifesto.ui.adapter.PeopleAdapter;
import com.example.stefan.manifesto.viewmodel.PeopleViewModel;

import java.util.List;

public class PeopleFragment extends BaseFragment implements PeopleAdapter.OnPersonItemClickListener {

    private PeopleViewModel viewModel;
    private FragmentPeopleBinding binding;
    private PeopleAdapter adapter;

    public static PeopleFragment newInstance() {
        return new PeopleFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_people, container, false);
            viewModel = ViewModelProviders.of(this).get(PeopleViewModel.class);
            binding.setViewModel(viewModel);
        }

        if (adapter != null) {
            binding.recyclerPeople.setAdapter(adapter);
        }

        initViews();
        setUpObservers();
        return binding.getRoot();
    }

    private void initViews() {
        binding.recyclerPeople.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerPeople.setHasFixedSize(true);
    }

    private void setUpObservers() {
        viewModel.getPeople().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                setAdapter(users);
            }
        });
    }

    @Override
    public void onPersonClick(User user) {
//        Intent intent = new Intent(getContext(), UserProfileActivity.class);
//        intent.putExtra(UserProfileActivity.EXTRA_USER_ID, user.getId());
//        startActivity(intent);
        Intent intent = new Intent(getContext(), MessagingActivity.class);
        intent.putExtra(MessagingActivity.EXTRA_USER_ID, user.getId());
        startActivity(intent);
    }

    private void setAdapter(List<User> users) {
        if (adapter == null) {
            adapter = new PeopleAdapter(users, this);
            binding.recyclerPeople.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}
