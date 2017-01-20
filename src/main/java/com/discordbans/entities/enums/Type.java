package com.discordbans.entities.enums;

/**
 * Copyright (C) DiscordBans 2017
 * This project is licensed under the GNU GPLv3 License.
 */
public enum Type {

    /**
     * USER - Used to represent a discord User (bots too).
     */
    USER,
    /**
     * GUILD - Used to represent a discord Guild (also known as Server).
     */
    GUILD;

    /**
     * @param name - The name of this type.
     * @return valueOf name ignoring case, null if not found.
     */
    public static Type fromString(String name) {
        for(Type type: Type.values()) {
            if(type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }
    
}
