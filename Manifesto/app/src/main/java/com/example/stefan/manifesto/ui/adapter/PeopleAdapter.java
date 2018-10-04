package com.example.stefan.manifesto.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stefan.manifesto.databinding.ItemPersonBinding;
import com.example.stefan.manifesto.model.User;
import com.example.stefan.manifesto.viewmodel.PersonItemViewModel;

import java.util.List;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.PeopleItemViewHolder> {

    private List<User> people;
    private OnPersonItemClickListener listener;

    public PeopleAdapter(List<User> people, OnPersonItemClickListener listener) {
        this.people = people;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PeopleItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PeopleItemViewHolder(ItemPersonBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleAdapter.PeopleItemViewHolder holder, int position) {
        final User user = people.get(position);
        holder.bind(new PersonItemViewModel(user));
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPersonClick(user);
            }
        });
        holder.binding.imageButtonMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPersonMessageClick(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return people != null ? people.size() : 0;
    }

    public class PeopleItemViewHolder extends RecyclerView.ViewHolder {
        private ItemPersonBinding binding;

        public PeopleItemViewHolder(ItemPersonBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(PersonItemViewModel viewModel) {
            binding.setViewModel(viewModel);
        }
    }

    public interface OnPersonItemClickListener {
        void onPersonClick(User user);
        void onPersonMessageClick(User user);
    }
}
