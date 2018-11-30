package com.drinkbird.restaurant.reservations.api.domain;

public class CreateReservationData extends ReservationCommandData {
    private final String email;
    private final String name;
    private final int seats;

    public CreateReservationData(String email, String name, int seats) {
        super(ReservationCommandType.CREATE);
        this.email = email;
        this.name = name;
        this.seats = seats;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public int getSeats() {
        return seats;
    }
}
