package com.example.gymmanager.models;

public class AdminStats {

    private int totalUsers;
    private int totalClasses;
    private int totalReservations;

    public AdminStats(
            int totalUsers,
            int totalClasses,
            int totalReservations
    ) {
        this.totalUsers = totalUsers;
        this.totalClasses = totalClasses;
        this.totalReservations = totalReservations;
    }

    public int getTotalUsers() {
        return totalUsers;
    }

    public int getTotalClasses() {
        return totalClasses;
    }

    public int getTotalReservations() {
        return totalReservations;
    }
}