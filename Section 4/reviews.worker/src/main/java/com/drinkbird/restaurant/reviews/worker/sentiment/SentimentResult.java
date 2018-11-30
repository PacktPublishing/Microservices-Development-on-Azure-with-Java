package com.drinkbird.restaurant.reviews.worker.sentiment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SentimentResult {
    private final SentimentResultDocument[] documents;
    private final String[] errors;

    @JsonCreator
    public SentimentResult(
        @JsonProperty("documents") SentimentResultDocument[] documents,
        @JsonProperty("errors") String[] errors
    ) {
        this.documents = documents;
        this.errors = errors;
    }

    public SentimentResultDocument[] getDocuments() {
        return documents;
    }

    public String[] getErrors() {
        return errors;
    }
}
