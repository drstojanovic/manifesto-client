package com.example.stefan.manifesto.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.example.stefan.manifesto.databinding.NotificationSettingsItemBinding;
import com.example.stefan.manifesto.model.NotificationsSettingsItem;
import com.example.stefan.manifesto.viewmodel.NotificationSettingsItemViewModel;

import java.util.List;

public class NotificationSettingsAdapter extends RecyclerView.Adapter<NotificationSettingsAdapter.NotificationSettingsViewHolder> {

    private List<NotificationsSettingsItem> items;
    private OnSettingSelectedListener listener;

    public NotificationSettingsAdapter(List<NotificationsSettingsItem> items, OnSettingSelectedListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotificationSettingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationSettingsViewHolder(NotificationSettingsItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent , false));

    }

    @Override
    public void onBindViewHolder(@NonNull NotificationSettingsViewHolder holder, int position) {
        final NotificationsSettingsItem item = items.get(position);
        holder.bind(new NotificationSettingsItemViewModel(item));

        holder.binding.radioBtnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setScope(NotificationsSettingsItem.Scope.ALL);
                listener.onCheckBoxSelected(item);
            }
        });
        holder.binding.radioBtnEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setScope(NotificationsSettingsItem.Scope.EMERGENCY);
                listener.onCheckBoxSelected(item);
            }
        });
        holder.binding.radioBtnEmergencyNerby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setScope(NotificationsSettingsItem.Scope.EMERGENCY_NEARBY);
                listener.onCheckBoxSelected(item);
            }
        });
        holder.binding.radioBtnNone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setScope(NotificationsSettingsItem.Scope.NONE);
                listener.onCheckBoxSelected(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public class NotificationSettingsViewHolder extends RecyclerView.ViewHolder {
        private NotificationSettingsItemBinding binding;

        public NotificationSettingsViewHolder(NotificationSettingsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(NotificationSettingsItemViewModel viewModel) {
            binding.setViewModel(viewModel);
            binding.executePendingBindings();
        }
    }

    public interface OnSettingSelectedListener {
        void onCheckBoxSelected(NotificationsSettingsItem item);
    }
}