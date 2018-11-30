package com.drinkbird.restaurant.reservations.api.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReservationCreatedData extends ReservationEventData {
    private final String name;
    private final String email;
    private final int seats;

    @JsonCreator
    public ReservationCreatedData(
        @JsonProperty("name") String name,
        @JsonProperty("email") String email,
        @JsonProperty("seats") int seats
    ) {
        super(ReservationEventType.CREATED);
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
