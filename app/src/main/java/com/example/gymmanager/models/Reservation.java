package com.example.gymmanager.models;

public class Reservation {

    private String reservaId;
    private String nombreCliente;
    private boolean asistio;

    public Reservation(String reservaId, String nombreCliente, boolean asistio) {
        this.reservaId = reservaId;
        this.nombreCliente = nombreCliente;
        this.asistio = asistio;
    }

    public String getReservaId() {
        return reservaId;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public boolean isAsistio() {
        return asistio;
    }
}