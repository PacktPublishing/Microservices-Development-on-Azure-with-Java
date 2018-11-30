package com.drinkbird.restaurant.reservations.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class ReservationEventData {
    private final ReservationEventType eventType;

    public ReservationEventData(ReservationEventType eventType) {
        this.eventType = eventType;
    }

    @JsonIgnore
    public ReservationEventType getEventType() {
        return eventType;
    }
}
