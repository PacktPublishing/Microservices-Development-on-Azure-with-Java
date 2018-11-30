package com.drinkbird.restaurant.reviews.worker.sentiment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SentimentResultDocument {
    private final String id;
    private final double score;

    @JsonCreator
    public SentimentResultDocument(
        @JsonProperty("id") String id,
        @JsonProperty("score") double score
    ) {
        this.id = id;
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public double getScore() {
        return score;
    }
}
