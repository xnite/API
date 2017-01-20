package com.discordbans.entities.enums;

/**
 * Copyright (C) DiscordBans 2017
 * This project is licensed under the GNU GPLv3 License.
 */
public enum Severity {
    
    /**
     *  EXTREME - Used when the severity is (really) extreme. [Insta-ban recommended]
     *  Eg: Raid/Spam Bots
     */
    EXTREME(10),
    /**
     *  HIGHEST - Used when the severity is super high.
     *  Eg: Ban Evasion, Discord TOS Violation (Child Porn, etc..)
     */
    HIGHEST(4),
    /**
     *  HIGH - Used when the severity is higher than normal.
     *  Eg: Spam, Threatening
     */
    HIGH(3),
    /**
     *  NORMAL - Used to represent a normal report.
     *  Eg: Advertising
     */
    NORMAL(2),
    /**
     *  LOW - Used to represent a report that isn't too "bad".
     *  Eg: Profanity
     */
    LOW(1),
    /**
     *  NONE - Default state for a report that wasn't yet verified by an admin.
     */
    NONE(0);

    private int value;

    Severity(int i) {
        this.value = i;
    }

    /**
     * @return int that represents the weight of this severity.
     */
    public int getValue() {
        return this.value;
    }

    /**
     * @param name - The name of this type.
     * @return valueOf name ignoring case, null if not found.
     */
    public static Severity fromString(String name) {
        for(Severity severity: Severity.values()) {
            if(severity.name().equalsIgnoreCase(name)) {
                return severity;
            }
        }
        return null;
    }

}
