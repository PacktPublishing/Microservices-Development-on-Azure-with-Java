package com.drinkbird.restaurant.reservations.api.domain;

public class Reservation {
    private final String name;
    private final String email;
    private final int seats;

    public Reservation(String name, String email, int seats) {
        this.name = name;
        this.email = email;
        this.seats = seats;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getSeats() {
        return seats;
    }
}
