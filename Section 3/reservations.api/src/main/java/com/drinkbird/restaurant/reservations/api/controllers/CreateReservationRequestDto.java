package com.drinkbird.restaurant.reservations.api.controllers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateReservationRequestDto {
    private final String name;
    private final String email;
    private final int seats;

    @JsonCreator
    public CreateReservationRequestDto(
        @JsonProperty("name") String name,
        @JsonProperty("email") String email,
        @JsonProperty("seats") int seats)
    {
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
