package com.discordbans.entities.enums;

/**
 * Copyright (C) DiscordBans 2017
 * This project is licensed under the GNU GPLv3 License.
 */
public enum State {

    /**
     * APPROVED - This report has been approved by an admin and has a severity.
     */
    APPROVED,
    /**
     * WAITING - This report is currently waiting revision by an admin.
     */
    WAITING,
    /**
     * WAITING_REPLY - This report is waiting reply from the submitter.
     */
    WAITING_REPLY,
    /**
     * REPLIED - This report was replied by the user and is now waiting for revision by an admin.
     */
    REPLIED,
    /**
     * DENIED - This report was denied and is not in effect.
     */
    DENIED;

    /**
     * @param name - The name of this type.
     * @return valueOf name ignoring case, null if not found.
     */
    public static State fromString(String name) {
        for(State state: State.values()) {
            if(state.name().equalsIgnoreCase(name)) {
                return state;
            }
        }
        return null;
    }
    
}
