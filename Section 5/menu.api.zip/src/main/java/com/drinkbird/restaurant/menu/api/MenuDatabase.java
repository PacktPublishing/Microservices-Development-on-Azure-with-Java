package com.drinkbird.restaurant.menu.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuDatabase {
    private final List<MenuItem> rawMenuItems =
        Arrays.asList(
            new MenuItem(
                "Cupcake",
                new URL("https://loremflickr.com/320/240/cupcake/all")),
            new MenuItem(
                "Coffee",
                new URL("https://loremflickr.com/320/240/coffee/all")),
            new MenuItem(
                "Bread",
                new URL("https://loremflickr.com/320/240/bread/all")));

    public MenuDatabase() throws MalformedURLException { }

    public List<MenuItem> getMenu() {
        logger.info("Retrieving menu from the database");
        return assignRandomImages(rawMenuItems);
    }

    private List<MenuItem> assignRandomImages(List<MenuItem> menuItems) {
        return
            menuItems
                .stream()
                .map(x -> {
                    URL randomUrl = getRandomImageUrl(x.getImageUrl());
                    return new MenuItem(x.getName(), randomUrl);
                })
                .collect(Collectors.toList());
    }

    private URL getRandomImageUrl(URL categoryUrl) {
        try {
            // loremflickr.com category urls redirect to
            // a different random image on every call
            logger.info("Getting random image url for: {}", categoryUrl);
            URLConnection connection = categoryUrl.openConnection();
            connection.addRequestProperty("User-Agent", "Chrome"); //LoremPixel will now deny requests without an agent
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            URL randomImageUrl = connection.getURL();
            inputStream.close();
            return randomImageUrl;
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static Logger logger = LoggerFactory.getLogger(MenuDatabase.class);
}
