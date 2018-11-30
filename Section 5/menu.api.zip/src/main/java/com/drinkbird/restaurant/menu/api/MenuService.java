package com.drinkbird.restaurant.menu.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {
    private final MenuDatabase menuDatabase;
    private final MenuCache menuCache;

    public MenuService(MenuDatabase menuDatabase, MenuCache menuCache) {
        this.menuDatabase = menuDatabase;
        this.menuCache = menuCache;
    }

    public List<MenuItem> getMenu() {
        List<MenuItem> menuFromCache = menuCache.getMenu();

        if (!menuFromCache.isEmpty()) {
            logger.info("Serving the menu from cache");
            return menuFromCache;
        }
        else {
            List<MenuItem> menuFromDatabase = menuDatabase.getMenu();
            menuCache.cacheMenu(menuFromDatabase);
            logger.info("Serving the menu from database");
            return menuFromDatabase;
        }
    }

    private static Logger logger = LoggerFactory.getLogger(MenuService.class);
}
