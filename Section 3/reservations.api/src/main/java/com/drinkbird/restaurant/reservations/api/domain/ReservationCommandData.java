package com.drinkbird.restaurant.reservations.api.domain;

public abstract class ReservationCommandData {
    private final ReservationCommandType commandType;

    protected ReservationCommandData(ReservationCommandType commandType) {
        this.commandType = commandType;
    }

    public ReservationCommandType getCommandType() {
        return commandType;
    }
}
