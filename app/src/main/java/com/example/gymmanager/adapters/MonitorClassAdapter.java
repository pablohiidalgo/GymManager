package com.example.gymmanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymmanager.R;
import com.example.gymmanager.models.GymClass;

import java.util.List;

public class MonitorClassAdapter extends RecyclerView.Adapter<MonitorClassAdapter.ViewHolder> {

    public interface OnClassClickListener {
        void onClassClick(GymClass gymClass);
    }

    private final List<GymClass> classList;
    private final OnClassClickListener listener;

    public MonitorClassAdapter(List<GymClass> classList, OnClassClickListener listener) {
        this.classList = classList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MonitorClassAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_class, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonitorClassAdapter.ViewHolder holder, int position) {
        GymClass gymClass = classList.get(position);

        holder.tvName.setText(gymClass.getNombre());
        holder.tvSchedule.setText("Horario: " + gymClass.getHorario());
        holder.tvCapacity.setText("Aforo máximo: " + gymClass.getAforoMaximo());

        holder.itemView.setOnClickListener(v -> listener.onClassClick(gymClass));
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvSchedule, tvCapacity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvAdminClassName);
            tvSchedule = itemView.findViewById(R.id.tvAdminClassSchedule);
            tvCapacity = itemView.findViewById(R.id.tvAdminClassCapacity);

            View btnEdit = itemView.findViewById(R.id.btnEditClass);
            btnEdit.setVisibility(View.GONE);
        }
    }
}