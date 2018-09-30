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
import com.example.stefan.manifesto.databinding.FragmentEventListBinding;
import com.example.stefan.manifesto.model.Event;
import com.example.stefan.manifesto.ui.activity.ShowEventActivity;
import com.example.stefan.manifesto.ui.adapter.EventAdapter;
import com.example.stefan.manifesto.viewmodel.EventListViewModel;

import java.util.List;

public class EventListFragment extends BaseFragment implements EventAdapter.OnEventItemClickListener {

    public static final String EXTRA_EVENT_ID = "EXTRA_EVENT_ID";
    private FragmentEventListBinding binding;
    private EventListViewModel viewModel;
    private EventAdapter adapter;

    public static EventListFragment newInstance() {
        return new EventListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_list, container, false);
            viewModel = ViewModelProviders.of(this).get(EventListViewModel.class);
            binding.setViewModel(viewModel);
        }
        setViews();
        setUpObservers();

        if (adapter != null) {
            binding.recyclerEvents.setAdapter(adapter);
        }

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.getAllEvents();
    }

    private void setViews() {
        binding.recyclerEvents.setHasFixedSize(true);
        binding.recyclerEvents.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setUpObservers() {
        viewModel.getEvents().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {
                if (events == null) {
                    makeToast(R.string.error);
                }
                setAdapter(events);
            }
        });
    }

    public void setAdapter(List<Event> events) {
        if (adapter == null) {
            adapter = new EventAdapter(events, this);
            binding.recyclerEvents.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onEventItemClick(Integer eventId) {
        Intent intent = new Intent(getContext(), ShowEventActivity.class);
        intent.putExtra(EXTRA_EVENT_ID, eventId);
        startActivity(intent);
    }

}
