package com.discordbans.entities;

import com.discordbans.entities.enums.Severity;
import com.discordbans.entities.enums.State;
import com.discordbans.entities.enums.Type;

import java.sql.Timestamp;

/**
 * Copyright (C) DiscordBans 2017
 * This project is licensed under the GNU GPLv3 License.
 */
public class BanReport {

    private String uuid;
    private Long reported;
    private Type type;
    private Long submitter;
    private State state;
    private Long admin;
    private Severity severity;
    private String reason;
    private Long guild;
    private Timestamp timestamp;
    
    public BanReport(String uuid, Long reported, Type type, Long submitter, State state, Long admin, 
                     Severity severity, String reason, Long guild, Timestamp timestamp) {
        this.uuid = uuid;
        this.reported = reported;
        this.type = type;
        this.submitter = submitter;
        this.state = state;
        this.admin = admin;
        this.severity = severity;
        this.reason = reason;
        this.guild = guild;
        this.timestamp = timestamp;
    }
    
    public String getUUID() {
        return this.uuid;
    }
    
    public Long getReportedId() {
        return this.reported;
    }
    
    public Type getReportedType() {
        return this.type;
    }
    
    public Long getSubmitter() {
        return this.submitter;
    }
    
    public State getState() {
        return this.state;
    }
    
    public Long getAdmin() {
        return this.admin;
    }
    
    public Severity getSeverity() {
        return this.severity;
    }
    
    public String getReason() {
        return this.reason;
    }
    
    public Long getGuild() {
        return this.guild;
    }
    
    public Timestamp getTimestamp() {
        return this.timestamp;
    }
    
}
