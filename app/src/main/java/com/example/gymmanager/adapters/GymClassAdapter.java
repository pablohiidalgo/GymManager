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

public class GymClassAdapter extends RecyclerView.Adapter<GymClassAdapter.GymClassViewHolder> {

    private final List<GymClass> classList;

    public GymClassAdapter(List<GymClass> classList) {
        this.classList = classList;
    }

    @NonNull
    @Override
    public GymClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_gym_class, parent, false);

        return new GymClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GymClassViewHolder holder, int position) {
        GymClass gymClass = classList.get(position);

        holder.tvClassName.setText(gymClass.getNombre());
        holder.tvClassDescription.setText(gymClass.getDescripcion());
        holder.tvClassSchedule.setText("Horario: " + gymClass.getHorario());
        holder.tvClassCapacity.setText("Aforo máximo: " + gymClass.getAforoMaximo());
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    public static class GymClassViewHolder extends RecyclerView.ViewHolder {

        TextView tvClassName, tvClassDescription, tvClassSchedule, tvClassCapacity;

        public GymClassViewHolder(@NonNull View itemView) {
            super(itemView);

            tvClassName = itemView.findViewById(R.id.tvClassName);
            tvClassDescription = itemView.findViewById(R.id.tvClassDescription);
            tvClassSchedule = itemView.findViewById(R.id.tvClassSchedule);
            tvClassCapacity = itemView.findViewById(R.id.tvClassCapacity);
        }
    }
}