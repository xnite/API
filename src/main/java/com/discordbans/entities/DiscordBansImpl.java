package com.discordbans.entities;

import com.discordbans.entities.enums.Type;
import com.discordbans.exceptions.DiscordBansException;

import java.util.List;

/**
 * Copyright (C) DiscordBans 2017
 * This project is licensed under the GNU GPLv3 License.
 */
public interface DiscordBansImpl {

    /**
     * @param uuid - The uuid of the report.
     * @return BanReport instance of the desired report.
     * @throws DiscordBansException - If any error occurs while contacting DiscordBans or there is no such report with that uuid.
     */
    BanReport getReport(String uuid) throws DiscordBansException;

    /**
     * @param id - The id of the guild (Discord Snowflake).
     * @return BanGuild instance of this guild.
     * @throws DiscordBansException - If any error occurs while contacting DiscordBans.
     */
    BanGuild getGuild(String id) throws DiscordBansException;

    /**
     * @param id - The id of the user (Discord Snowflake).
     * @return BanUser instance of this user.
     * @throws DiscordBansException - If any error occurs while contacting DiscordBans or if not found.
     */
    BanUser getUser(String id) throws DiscordBansException;

    /**
     * @param uuid - The uuid of the comment.
     * @return BanComment instance of this comment.
     * @throws DiscordBansException - If any error occurs while contacting DiscordBans or if not found.
     */
    BanComment getComment(String uuid) throws DiscordBansException;

    /**
     * @param uuid - The uuid of the report.
     * @return List<BancComment> with all comments for the given report.
     * @throws DiscordBansException - If any error occurs while contacting DiscordBans.
     */
    List<BanComment> getComments(String uuid) throws DiscordBansException;

    /**
     * @param id - The id to get the reports for.
     * @param type - The type (USER or GUILD).
     * @return List<BanReport> with all the reports for the given id and type.
     * @throws DiscordBansException - If any error occurs while contacting DiscordBans.
     */
    List<BanReport> getReports(String id, Type type) throws DiscordBansException;

    /**
     * @param id - The id to report.
     * @param type - The type (USER or GUILD).
     * @param reason - The reason for reporting.
     * @param guild - The guild where this report is originating (null for none).
     * @param submitter - The user id of the person who is submitting this report. (null for default)
     * @return BanReport instance of this report.
     * @throws DiscordBansException - If any error occurs while contacting DiscordBans.
     */
    BanReport addReport(String id, Type type, String reason, Long guild, BanUser submitter) throws DiscordBansException;
    
    /**
     * @param id - The id to report.
     * @param type - The type (USER or GUILD).
     * @param reason - The reason for reporting.
     * @param guild - The guild where this report is originating (null for none).
     * @return BanReport instance of this report.
     * @throws DiscordBansException - If any error occurs while contacting DiscordBans.
     */
    BanReport addReport(String id, Type type, String reason, Long guild) throws DiscordBansException;

    /**
     * @param id - The id to report.
     * @param type - The type (USER or GUILD).
     * @param reason - The reason for reporting.
     * @param submitter - The user id of the person who is submitting this report. (null for default)
     * @return BanReport instance of this report.
     * @throws DiscordBansException - If any error occurs while contacting DiscordBans.
     */
    BanReport addReport(String id, Type type, String reason, BanUser submitter) throws DiscordBansException;

    /**
     * @param id - The id to report.
     * @param type - The type (USER or GUILD).
     * @param reason - The reason for reporting.
     * @return BanReport instance of this report.
     * @throws DiscordBansException - If any error occurs while contacting DiscordBans.
     */
    BanReport addReport(String id, Type type, String reason) throws DiscordBansException;

    /**
     * @param id - The id to check.
     * @param type - User or Guild
     * @return true if this user/guild is considered banned.
     * @throws DiscordBansException - If any error occurs while contacting DiscordBans.
     */
    Boolean isBanned(String id, Type type) throws DiscordBansException;

    /**
     * @param uuid - The uuid of the report.
     * @param content - The content of the comment.
     * @return List<BanComment> with all comments for this report (including the new one).
     * @throws DiscordBansException - If any error occurs while contacting DiscordBans.
     */
    List<BanComment> addComment(String uuid, String content) throws DiscordBansException;
        
}
