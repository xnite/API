package com.discordbans;

import java.util.concurrent.TimeUnit;

/**
 * Copyright (C) DiscordBans 2017
 * This project is licensed under the GNU GPLv3 License.
 */
public class DBansBuilder {
    
    private Boolean usingCache = false;
    private TimeUnit timeUnit = TimeUnit.SECONDS;
    private Long time = 30L;
    private String key;
    
    public DBansBuilder(String key) {
        this.key = key;
    }
    
    public DBansBuilder useCache(Boolean cache) {
        this.usingCache = cache;
        return this;
    }
    
    public DBansBuilder expireTime(Long value, TimeUnit timeUnit) {
        this.time = value;
        this.timeUnit = timeUnit;
        return this;
    }
    
    public DBansBuilder expireTime(Integer value, TimeUnit timeUnit) {
        this.time = Long.valueOf(value.toString());
        this.timeUnit = timeUnit;
        return this;
    }
    
    public DiscordBans build() {
        return new DiscordBans(key, usingCache, timeUnit, time);
    }
    
}
