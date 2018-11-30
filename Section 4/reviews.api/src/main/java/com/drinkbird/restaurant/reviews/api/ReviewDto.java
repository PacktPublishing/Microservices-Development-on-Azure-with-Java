package com.drinkbird.restaurant.reviews.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReviewDto {
    private final String name;
    private final String email;
    private final String comment;
    private final ImageBase64 imageBase64; // data:image/gif;base64,R0lGODl...

    @JsonCreator
    public ReviewDto(
        @JsonProperty("name") String name,
        @JsonProperty("email") String email,
        @JsonProperty("comment") String comment,
        @JsonProperty("imageBase64") String imageBase64Str
    ) {
        this.name = name;
        this.email = email;
        this.comment = comment;
        this.imageBase64 = ImageBase64.parse(imageBase64Str);
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

    public ImageBase64 getImageBase64() {
        return imageBase64;
    }
}
