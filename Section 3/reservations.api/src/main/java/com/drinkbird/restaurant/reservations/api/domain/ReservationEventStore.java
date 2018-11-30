package com.drinkbird.restaurant.reservations.api.domain;

import com.drinkbird.restaurant.reservations.api.repositories.RawEvent;
import com.drinkbird.restaurant.reservations.api.repositories.RawEventsRepository;
import org.bson.Document;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationEventStore {
    private final RawEventsRepository repository;

    public ReservationEventStore(RawEventsRepository repository) {
        this.repository = repository;
    }

    public List<ReservationEvent> GetEvents(DateId dateId) {
        return repository
            .findByDate(dateId.toString())
            .stream()
            .sorted(Comparator.comparing(RawEvent::getSequenceNumber))
            .map(ReservationEventStore::mapFromRaw)
            .collect(Collectors.toList());
    }

    private static ReservationEvent mapFromRaw(RawEvent rawEvent) {
        ReservationEventType eventType =
            ReservationEventType.parse(rawEvent.getEventType());

        ReservationEventData eventData;
        switch (eventType) {
            case CREATED:
                eventData = rawEvent.parseEventData(ReservationCreatedData.class);
                break;
            case CANCELLED:
                eventData = rawEvent.parseEventData(ReservationCancelledData.class);
                break;
            default:
                throw new RuntimeException(
                    "Unsupported rawEvent type: " + rawEvent.getEventType());
        }

        return new ReservationEvent(
            DateId.parse(rawEvent.getDate()),
            rawEvent.getSequenceNumber(),
            DateTime.parse(rawEvent.getCreatedOn()),
            eventData
        );
    }

    public void PersistEvents(List<ReservationEvent> events) {
        List<RawEvent> rawEvents =
            events
            .stream()
            .map(e -> {
                String dateIdStr = e.getDateId().toString();
                String eventId = dateIdStr + "_" + e.getSequenceNumber();
                String eventType = e.getEventData().getEventType().toString();
                Document eventData = RawEvent.parseEventData(e.getEventData());
                RawEvent rawEvent =
                    new RawEvent(eventId, dateIdStr, e.getSequenceNumber(),
                        e.getCreatedOn().toString(), eventType, eventData);
                return rawEvent;
            })
            .collect(Collectors.toList());
        repository.saveAll(rawEvents);
    }
}
