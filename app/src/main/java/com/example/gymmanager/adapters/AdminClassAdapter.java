package com.example.gymmanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymmanager.R;
import com.example.gymmanager.models.GymClass;

import java.util.List;

public class AdminClassAdapter extends RecyclerView.Adapter<AdminClassAdapter.AdminClassViewHolder> {

    public interface OnEditClickListener {
        void onEditClick(GymClass gymClass);
    }

    private final List<GymClass> classList;
    private final OnEditClickListener listener;

    public AdminClassAdapter(List<GymClass> classList, OnEditClickListener listener) {
        this.classList = classList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdminClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_class, parent, false);

        return new AdminClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminClassViewHolder holder, int position) {
        GymClass gymClass = classList.get(position);

        holder.tvAdminClassName.setText(gymClass.getNombre());
        holder.tvAdminClassSchedule.setText("Horario: " + gymClass.getHorario());
        holder.tvAdminClassCapacity.setText("Aforo máximo: " + gymClass.getAforoMaximo());

        holder.btnEditClass.setOnClickListener(v -> listener.onEditClick(gymClass));
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    public static class AdminClassViewHolder extends RecyclerView.ViewHolder {

        TextView tvAdminClassName, tvAdminClassSchedule, tvAdminClassCapacity;
        Button btnEditClass;

        public AdminClassViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAdminClassName = itemView.findViewById(R.id.tvAdminClassName);
            tvAdminClassSchedule = itemView.findViewById(R.id.tvAdminClassSchedule);
            tvAdminClassCapacity = itemView.findViewById(R.id.tvAdminClassCapacity);
            btnEditClass = itemView.findViewById(R.id.btnEditClass);
        }
    }
}