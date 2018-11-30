package com.drinkbird.restaurant.menu.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
public class MenuCache {
    private static final String menuEntryKey = "menu";
    private final String host;
    private final String authKey;
    private final ObjectMapper mapper;

    public MenuCache(Environment env) {
        this.host = Objects.requireNonNull(
            env.getProperty("azure.redis.host"));
        this.authKey = Objects.requireNonNull(
            env.getProperty("azure.redis.key"));
        this.mapper = new ObjectMapper();
    }

    private Jedis createClient() {
        JedisShardInfo shardInfo = new JedisShardInfo(this.host, 6380, true);
        shardInfo.setPassword(this.authKey);
        return new Jedis(shardInfo);
    }

    public List<MenuItem> getMenu() {
        logger.info("Attempting to retrieve the menu from cache");

        Jedis client = createClient();
        String resultString = client.get(menuEntryKey);
        client.close();

        if(resultString != null && !resultString.isEmpty()) {
            try {
                return mapper.readValue(resultString,
                    new TypeReference<List<MenuItem>>() { });
            }
            catch (IOException e) {
                logger.error("Cache content is of unknown format: {}", resultString);
                return new LinkedList<>();
            }
        }
        else {
            return new LinkedList<>();
        }
    }

    public void cacheMenu(List<MenuItem> menu) {
        logger.info("Attempting to cache the menu");
        try {
            String menuAsString = mapper.writeValueAsString(menu);

            Jedis client = createClient();
            client.set(menuEntryKey, menuAsString);
            client.close();
        }
        catch (IOException ex) {
            logger.error("Could not cache the menu: {}", ex.getMessage(), ex);
        }
    }

    private static Logger logger = LoggerFactory.getLogger(MenuCache.class);
}
