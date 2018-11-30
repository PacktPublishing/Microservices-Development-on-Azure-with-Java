package com.drinkbird.restaurant.reservations.api.repositories;

import com.drinkbird.restaurant.reservations.api.domain.ReservationCreatedData;
import com.drinkbird.restaurant.reservations.api.domain.ReservationEventData;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reservations")
public class RawEvent {
    @Id
    private final String id;
    private final String date;
    private final long sequenceNumber;
    private final String createdOn;
    private final String eventType;
    private final org.bson.Document eventData;

    public RawEvent(
        @JsonProperty("id") String id,
        @JsonProperty("date") String date,
        @JsonProperty("sequenceNumber") long sequenceNumber,
        @JsonProperty("createdOn") String createdOn,
        @JsonProperty("eventType") String eventType,
        @JsonProperty("eventData") org.bson.Document eventData
    ) {
        this.id = id;
        this.date = date;
        this.sequenceNumber = sequenceNumber;
        this.createdOn = createdOn;
        this.eventType = eventType;
        this.eventData = eventData;
    }

    public static org.bson.Document parseEventData(ReservationEventData eventData) {
        try {
            String jsonString = mapper.writeValueAsString(eventData);
            return org.bson.Document.parse(jsonString);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getEventType() {
        return eventType;
    }

    public org.bson.Document getEventData() {
        return eventData;
    }

    public <T> T parseEventData(Class type) {
        try {
            return (T)mapper.readValue(getEventData().toJson(), type);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static ObjectMapper mapper = new ObjectMapper();
}
