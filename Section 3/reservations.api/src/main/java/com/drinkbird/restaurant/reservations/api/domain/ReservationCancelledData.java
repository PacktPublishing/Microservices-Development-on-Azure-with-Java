package com.drinkbird.restaurant.reservations.api.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReservationCancelledData extends ReservationEventData {
    private final String email;

    @JsonCreator
    public ReservationCancelledData(
        @JsonProperty("email") String email
    ) {
        super(ReservationEventType.CANCELLED);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
