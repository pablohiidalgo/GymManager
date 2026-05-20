package com.example.gymmanager.models;

public class GymClass {

    private String id;
    private String nombre;
    private String descripcion;
    private String horario;
    private int aforoMaximo;

    public GymClass(String id, String nombre, String descripcion, String horario, int aforoMaximo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.horario = horario;
        this.aforoMaximo = aforoMaximo;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getHorario() {
        return horario;
    }

    public int getAforoMaximo() {
        return aforoMaximo;
    }
}