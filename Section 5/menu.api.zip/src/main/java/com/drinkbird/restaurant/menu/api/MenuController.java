package com.drinkbird.restaurant.menu.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MenuController {
    private final MenuService menuService;
    private final MenuDatabase menuDatabase;

    public MenuController(MenuService menuService, MenuDatabase menuDatabase) {
        this.menuService = menuService;
        this.menuDatabase = menuDatabase;
    }

    // Cache-enabled expensive call
    @RequestMapping(value = "menu", method = RequestMethod.GET)
    public List<MenuItem> GetMenu() {
        return menuService.getMenu();
    }

    // Always expensive call
    @RequestMapping(value = "menu/from-db", method = RequestMethod.GET)
    public List<MenuItem> GetMenuFromDb() {
        return menuDatabase.getMenu();
    }
}
