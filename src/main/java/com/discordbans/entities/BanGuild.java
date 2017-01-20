package com.discordbans.entities;

/**
 * Copyright (C) DiscordBans 2017
 * This project is licensed under the GNU GPLv3 License.
 */
public class BanGuild {
    
    private String id;
    private String name;
    private Double reputation;
    
    public BanGuild(String id, String name, double reputation) {
        this.id = id;
        this.name = name;
        this.reputation = reputation;
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Double getReputation() {
        return this.reputation;
    }
    
}
