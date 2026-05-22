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

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder> {

    private final List<GymClass> reservations;

    public ReservationAdapter(List<GymClass> reservations) {
        this.reservations = reservations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gym_class, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        GymClass gymClass = reservations.get(position);

        holder.tvClassName.setText(gymClass.getNombre());

        holder.tvClassDescription.setText(
                gymClass.getDescripcion()
        );

        holder.tvClassSchedule.setText(
                "Horario: " + gymClass.getHorario()
        );

        holder.tvClassCapacity.setText(
                "Aforo máximo: " + gymClass.getAforoMaximo()
        );

        holder.btnReserve.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvClassName;
        TextView tvClassDescription;
        TextView tvClassSchedule;
        TextView tvClassCapacity;

        View btnReserve;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvClassName =
                    itemView.findViewById(R.id.tvClassName);

            tvClassDescription =
                    itemView.findViewById(R.id.tvClassDescription);

            tvClassSchedule =
                    itemView.findViewById(R.id.tvClassSchedule);

            tvClassCapacity =
                    itemView.findViewById(R.id.tvClassCapacity);

            btnReserve =
                    itemView.findViewById(R.id.btnReserveClass);
        }
    }
}