package com.example.stefan.manifesto.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stefan.manifesto.databinding.ItemEventBinding;
import com.example.stefan.manifesto.model.Event;
import com.example.stefan.manifesto.viewmodel.EventItemViewModel;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventItemViewHolder> {

    private List<Event> events;
    private OnEventItemClickListener listener;

    public EventAdapter(List<Event> eventList, OnEventItemClickListener listener) {
        this.events = eventList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventItemViewHolder(ItemEventBinding.inflate(LayoutInflater.from(parent.getContext()), parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventItemViewHolder holder, int position) {
        final Event event = events.get(position);
        holder.bind(new EventItemViewModel(event));
        holder.binding.cardEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onEventItemClick(event.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class EventItemViewHolder extends RecyclerView.ViewHolder {
        ItemEventBinding binding;

        EventItemViewHolder(ItemEventBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(EventItemViewModel viewModel) {
            binding.setViewModel(viewModel);
            binding.executePendingBindings();
        }
    }

    public interface OnEventItemClickListener {
        void onEventItemClick(Integer eventId);
    }


}
