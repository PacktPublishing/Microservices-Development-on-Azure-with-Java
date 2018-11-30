package com.drinkbird.restaurant.reservations.api.domain;

import org.joda.time.DateTime;

public class ReservationEvent {
    private final DateId dateId;
    private final long sequenceNumber;
    private final DateTime createdOn;
    private final ReservationEventData eventData;

    public ReservationEvent(
        DateId dateId,
        long sequenceNumber,
        DateTime createdOn,
        ReservationEventData eventData)
    {
        this.dateId = dateId;
        this.sequenceNumber = sequenceNumber;
        this.createdOn = createdOn;
        this.eventData = eventData;
    }

    public DateId getDateId() {
        return dateId;
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    public DateTime getCreatedOn() {
        return createdOn;
    }

    public ReservationEventData getEventData() {
        return eventData;
    }
}
