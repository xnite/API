package com.discordbans.entities;

/**
 * Copyright (C) DiscordBans 2017
 * This project is licensed under the GNU GPLv3 License.
 */
public class BanUser {
    
    private String id;
    private boolean admin;
    private String username;
    private Long discriminator;
    
    public BanUser(String id, boolean admin, String username, Long discriminator) {
        this.id = id;
        this.admin = admin;
        this.username = username;
        this.discriminator = discriminator;
    }
    
    public String getId() {
        return this.id;
    }
    
    public Boolean isAdmin() {
        return this.admin;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public Long getDiscriminator() {
        return this.discriminator;
    }
    
}
