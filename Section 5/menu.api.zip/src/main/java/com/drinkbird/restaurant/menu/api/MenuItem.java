package com.drinkbird.restaurant.menu.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URL;

public class MenuItem {
    private final String name;
    private final URL imageUrl;

    @JsonCreator
    public MenuItem(
        @JsonProperty("name") String name,
        @JsonProperty("imageUrl") URL imageUrl)
    {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public URL getImageUrl() {
        return imageUrl;
    }
}
