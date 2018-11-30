package com.drinkbird.restaurant.reservations.api.domain;

public class ReservationException extends Exception {
    public ReservationException(String reason) {
        super(reason);
    }
}
