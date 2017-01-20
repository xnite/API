package com.discordbans.entities;

import java.sql.Timestamp;

/**
 * Copyright (C) DiscordBans 2017
 * This project is licensed under the GNU GPLv3 License.
 */
public class BanComment {
    
    private Long submitter;
    private String report;
    private String uuid;
    private String content;
    private Timestamp timestamp;
    
    public BanComment(Long submitter, String report, String uuid, String content, Timestamp timestamp) {
        this.submitter = submitter;
        this.report = report;
        this.uuid = uuid;
        this.content = content;
        this.timestamp = timestamp;
    }
    
    public Long getSubmitter() {
        return this.submitter;
    }
    
    public String getReportId() {
        return this.report;
    }
    
    public String getUUID() {
        return this.uuid;
    }
    
    public String getContent() {
        return this.content;
    }
    
    public Timestamp getTimestamp() {
        return this.timestamp;
    }
    
}
