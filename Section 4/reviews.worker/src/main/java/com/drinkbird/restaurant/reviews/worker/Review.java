package com.drinkbird.restaurant.reviews.worker;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

import java.net.URI;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Review {
    private final String name;
    private final String email;
    private final String comment;
    private final URI imageUri;
    private final DateTime reviewedOn;

    @JsonCreator
    public Review(
        @JsonProperty("name") String name,
        @JsonProperty("email") String email,
        @JsonProperty("comment") String comment,
        @JsonProperty("imageUri") URI imageUri,
        @JsonProperty("reviewedOn") DateTime reviewedOn) {
        this.name = name;
        this.email = email;
        this.comment = comment;
        this.imageUri = imageUri;
        this.reviewedOn = reviewedOn;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getComment() {
        return comment;
    }

    public URI getImageUri() {
        return imageUri;
    }

    public DateTime getReviewedOn() {
        return reviewedOn;
    }
}
