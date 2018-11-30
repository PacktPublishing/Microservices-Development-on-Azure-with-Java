package com.drinkbird.restaurant.reservations.api.domain;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.List;

public class DateAggregate {
    static final int seatingCapacity = 10;

    private final DateId dateId;
    private final HashMap<String, Reservation> reservations;
    private long sequenceNumber;
    private DateTime lastUpdatedOn;

    public DateAggregate(DateId dateId) {
        this.dateId = dateId;
        this.reservations = new HashMap<>();
        this.sequenceNumber = -1;
        this.lastUpdatedOn = DateTime.now();
    }

    public DateId getDateId() {
        return dateId;
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public DateTime getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(DateTime lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public boolean reservationExists(String email) {
        return reservations.containsKey(email);
    }

    public void addReservation(Reservation reservation) {
        reservations.put(reservation.getEmail(), reservation);
    }

    public void removeReservation(String email) {
        reservations.remove(email);
    }

    public int getAvailableSeats() {
        int reservedSeats =
            reservations
                .values()
                .stream()
                .mapToInt(r -> r.getSeats())
                .sum();
        int availableSeats = seatingCapacity - reservedSeats;
        return availableSeats >= 0 ? availableSeats : 0;
    }

    public void apply(List<ReservationEvent> events) {
        events
            .stream()
            .forEach(event -> {
                ReservationEventData eventData = event.getEventData();
                switch(eventData.getEventType()) {
                    case CREATED:
                        ReservationCreatedData resCreated =
                            (ReservationCreatedData) eventData;
                        Reservation newReservation = new Reservation(
                            resCreated.getName(),
                            resCreated.getEmail(),
                            resCreated.getSeats());
                        addReservation(newReservation);
                        break;
                    case CANCELLED:
                        ReservationCancelledData resCancelled =
                            (ReservationCancelledData) eventData;
                        removeReservation(resCancelled.getEmail());
                        break;
                    default:
                        throw new RuntimeException("Unsupported eventData type");
                }
                setLastUpdatedOn(event.getCreatedOn());
                setSequenceNumber(event.getSequenceNumber());
            });
    }
}
