package com.example.gymmanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymmanager.R;
import com.example.gymmanager.models.Reservation;

import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    public interface OnAttendanceClick {
        void onMarkAttendance(Reservation reservation);
    }

    private final List<Reservation> reservations;
    private final OnAttendanceClick listener;

    public AttendanceAdapter(List<Reservation> reservations,
                             OnAttendanceClick listener) {

        this.reservations = reservations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_attendance, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Reservation reservation = reservations.get(position);

        holder.tvName.setText(reservation.getNombreCliente());

        if (reservation.isAsistio()) {
            holder.tvStatus.setText("Asistencia confirmada");
            holder.btnAttendance.setEnabled(false);
        } else {
            holder.tvStatus.setText("Pendiente");
            holder.btnAttendance.setEnabled(true);
        }

        holder.btnAttendance.setOnClickListener(v ->
                listener.onMarkAttendance(reservation)
        );
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvStatus;
        Button btnAttendance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvAttendanceName);
            tvStatus = itemView.findViewById(R.id.tvAttendanceStatus);
            btnAttendance = itemView.findViewById(R.id.btnAttendance);
        }
    }
}