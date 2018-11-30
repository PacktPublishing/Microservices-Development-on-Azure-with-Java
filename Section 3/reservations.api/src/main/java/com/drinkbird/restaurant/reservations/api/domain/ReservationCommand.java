package com.drinkbird.restaurant.reservations.api.domain;

import org.joda.time.DateTime;

public class ReservationCommand {
    private final DateId dateId;
    private final DateTime issuedOn;
    private final ReservationCommandData commandData;

    public ReservationCommand(
        DateId dateId,
        DateTime issuedOn,
        ReservationCommandData commandData)
    {
        this.dateId = dateId;
        this.issuedOn = issuedOn;
        this.commandData = commandData;
    }

    public DateId getDateId() {
        return dateId;
    }

    public DateTime getIssuedOn() {
        return issuedOn;
    }

    public ReservationCommandData getCommandData() {
        return commandData;
    }
}
