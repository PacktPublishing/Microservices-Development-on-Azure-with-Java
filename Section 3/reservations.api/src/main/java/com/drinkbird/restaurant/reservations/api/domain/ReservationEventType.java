package com.drinkbird.restaurant.reservations.api.domain;

import java.util.Arrays;

public enum ReservationEventType {
    CREATED("reservationCreated/1"),
    CANCELLED("reservationCancelled/1");

    private final String text;

    ReservationEventType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static ReservationEventType parse(String textValue) {
        return Arrays.stream(ReservationEventType.values())
            .filter(enumValue ->
                enumValue.text.equalsIgnoreCase(textValue))
            .findFirst()
            .orElseThrow(() ->
                new RuntimeException("unknown value: " + textValue));
    }
}
