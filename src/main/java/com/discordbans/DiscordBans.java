package com.discordbans;

import com.discordbans.entities.BanComment;
import com.discordbans.entities.BanGuild;
import com.discordbans.entities.BanReport;
import com.discordbans.entities.BanUser;
import com.discordbans.entities.enums.Severity;
import com.discordbans.entities.enums.State;
import com.discordbans.entities.enums.Type;
import com.discordbans.exceptions.DiscordBansException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.body.MultipartBody;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) DiscordBans 2017
 * This project is licensed under the GNU GPLv3 License.
 */
@SuppressWarnings("WeakerAccess")
public class DiscordBans {
    
    private String apiKey;
    
    public DiscordBans(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * @param uuid - The uuid of the report.
     * @return BanReport instance of the desired report.
     * @throws DiscordBansException - If any error occurs while contacting DiscordBans or there is no such report with that uuid.
     */
    public BanReport getReport(String uuid) throws DiscordBansException {
        try {
            HttpResponse<JsonNode> resp = Unirest.get("https://api.discordbans.com/getReport")
                    .header("API", this.apiKey)
                    .queryString("uuid", uuid).asJson();
            if(resp.getBody().getObject().has("error")) {
                throw new DiscordBansException(resp.getBody().getObject().getString("error"));
            }
            JSONObject object = resp.getBody().getObject();
            Severity severity = Severity.fromString(object.getString("severity"));
            Long submitter = object.has("submitter") ? object.getLong("submitter") : 250758034551341056L;
            String reason = object.getString("reason");
            Long guild = object.getLong("guild");
            Long reported = object.getLong("reported");
            Long admin = object.getLong("admin");
            State state = State.fromString(object.getString("state"));
            Type type = Type.fromString(object.getString("type"));
            Timestamp timestamp = Timestamp.valueOf(object.getString("timestamp"));
            return new BanReport(uuid, reported, type, submitter, state, admin, severity, reason, guild, timestamp);
        } catch (UnirestException e) {
            throw new DiscordBansException("Failed to contact DiscordBans");
        }
    }

    /**
     * @param id - The id of the guild (Discord Snowflake).
     * @return BanGuild instance of this guild.
     * @throws DiscordBansException - If any error occurs while contacting DiscordBans.
     */
    public BanGuild getGuild(String id) throws DiscordBansException {
        try {
            HttpResponse<JsonNode> resp = Unirest.get("https://api.discordbans.com/getGuild")
                    .header("API", this.apiKey)
                    .queryString("id", id).asJson();
            if(resp.getBody().getObject().has("error")) {
                throw new DiscordBansException(resp.getBody().getObject().getString("error"));
            }
            JSONObject object = resp.getBody().getObject();
            return new BanGuild(object.getString("id"), object.getString("name"), object.getDouble("reputation"));
        } catch (UnirestException e) {
            throw new DiscordBansException("Failed to contact DiscordBans");
        }
    }

    /**
     * @param id - The id of the user (Discord Snowflake).
     * @return BanUser instance of this user.
     * @throws DiscordBansException - If any error occurs while contacting DiscordBans or if not found.
     */
    public BanUser getUser(String id) throws DiscordBansException {
        try {
            HttpResponse<JsonNode> resp = Unirest.get("https://api.discordbans.com/getUser")
                    .header("API", this.apiKey)
                    .queryString("id", id).asJson();
            if(resp.getBody().getObject().has("error")) {
                throw new DiscordBansException(resp.getBody().getObject().getString("error"));
            }
            JSONObject object = resp.getBody().getObject();
            return new BanUser(object.getString("id"), object.getBoolean("admin"), object.getString("username"), object.getLong("discriminator"));
        } catch (UnirestException e) {
            throw new DiscordBansException("Failed to contact DiscordBans");
        }
    }

    /**
     * @param uuid - The uuid of the comment.
     * @return BanComment instance of this comment.
     * @throws DiscordBansException - If any error occurs while contacting DiscordBans or if not found.
     */
    public BanComment getComment(String uuid) throws DiscordBansException {
        try {
            HttpResponse<JsonNode> resp = Unirest.get("https://api.discordbans.com/getComment")
                    .header("API", this.apiKey)
                    .queryString("uuid", uuid).asJson();
            if(resp.getBody().getObject().has("error")) {
                throw new DiscordBansException(resp.getBody().getObject().getString("error"));
            }
            JSONObject object = resp.getBody().getObject();
            Long submitter = object.getLong("submitter");
            String report = object.getString("report");
            String content = object.getString("content");
            String rawstamp = object.getString("timestamp");
            return new BanComment(submitter, report, uuid, content, Timestamp.valueOf(rawstamp));
        } catch (UnirestException e) {
            throw new DiscordBansException("Failed to contact DiscordBans");
        }
    }

    /**
     * @param uuid - The uuid of the report.
     * @return List<BancComment> with all comments for the given report.
     * @throws DiscordBansException - If any error occurs while contacting DiscordBans.
     */
    public List<BanComment> getComments(String uuid) throws DiscordBansException {
        List<BanComment> comments = new ArrayList<>();
        try {
            HttpResponse<JsonNode> resp = Unirest.get("https://api.discordbans.com/getComments")
                    .header("API", this.apiKey)
                    .queryString("uuid", uuid).asJson();
            if(resp.getBody().getObject().has("error")) {
                throw new DiscordBansException(resp.getBody().getObject().getString("error"));
            }
            JSONArray object = resp.getBody().getObject().getJSONArray("entries");
            for(int i = 0; i < object.length(); i++) {
                JSONObject comment = object.getJSONObject(i);
                Long submitter = comment.getLong("submitter");
                String content = comment.getString("content");
                String rawstamp = comment.getString("timestamp");
                comments.add(new BanComment(submitter, uuid, uuid, content, Timestamp.valueOf(rawstamp)));
            }
        } catch (UnirestException ex) {
            throw new DiscordBansException("Failed to contact DiscordBans");
        }
        return comments;
    }

    /**
     * @param id - The id to get the reports for.
     * @param type - The type (USER or GUILD).
     * @return List<BanReport> with all the reports for the given id and type.
     * @throws DiscordBansException - If any error occurs while contacting DiscordBans.
     */
    public List<BanReport> getReports(String id, Type type) throws DiscordBansException {
        try {
            Long.valueOf(id);
        } catch (IllegalArgumentException ex) {
            throw new DiscordBansException("Invalid id");
        }
        List<BanReport> reports = new ArrayList<>();
        try {
            HttpResponse<JsonNode> resp = Unirest.get("https://api.discordbans.com/getReports")
                    .header("API", this.apiKey)
                    .queryString("id", id)
                    .queryString("type", type.name()).asJson();
            if(resp.getBody().getObject().has("error")) {
                throw new DiscordBansException(resp.getBody().getObject().getString("error"));
            }
            JSONArray object = resp.getBody().getObject().getJSONArray("entries");
            for(int i = 0; i < object.length(); i++) {
                JSONObject report = object.getJSONObject(i);
                String uuid = report.getString("uuid");
                Severity severity = Severity.fromString(report.getString("severity"));
                Long submitter = report.has("submitter") ? report.getLong("submitter") : 250758034551341056L;
                String reason = report.getString("reason");
                Long guild = report.getLong("guild");
                Long admin = report.getLong("admin");
                State state = State.fromString(report.getString("state"));
                Timestamp timestamp = Timestamp.valueOf(report.getString("timestamp"));
                reports.add(new BanReport(uuid, Long.valueOf(id), type, submitter, state, admin, severity, reason, guild, timestamp));
            }
        } catch (UnirestException ex) {
            throw new DiscordBansException("Failed to contact DiscordBans");
        }
        return reports;
    }

    /**
     * @param id - The id to report.
     * @param type - The type (USER or GUILD).
     * @param reason - The reason for reporting.
     * @param guild - The guild where this report is originating (null for none).
     * @param submitter - The user id of the person who is submitting this report. (null for default)
     * @return BanReport instance of this report.
     * @throws DiscordBansException - If any error occurs while contacting DiscordBans.
     */
    public BanReport addReport(String id, Type type, String reason, Long guild, BanUser submitter) throws DiscordBansException {
        try {
            MultipartBody mpBody = Unirest.post("https://api.discordbans.com/addReport")
                    .header("API", this.apiKey)
                    .field("id", id)
                    .field("type", type.name())
                    .field("reason", reason);
            if(submitter != null) {
                mpBody = mpBody.field("submitter", submitter.getId());
            }
            if(guild != null) {
                mpBody = mpBody.field("guild", guild);
            }
            HttpResponse<JsonNode> resp = mpBody.asJson();
            if(resp.getBody().getObject().has("error")) {
                throw new DiscordBansException(resp.getBody().getObject().getString("error"));
            }
            JSONObject object = resp.getBody().getObject();
            Long s = object.getLong("submitter");
            Severity severity = Severity.fromString(object.getString("severity"));
            Long reported = object.getLong("reported");
            Long admin = object.getLong("admin");
            State state = State.fromString(object.getString("state"));
            Timestamp timestamp = Timestamp.valueOf(object.getString("timestamp"));
            return new BanReport(object.getString("uuid"), reported, type, s, state, admin, severity, reason, guild, timestamp);
        } catch (UnirestException e) {
            throw new DiscordBansException("Failed to contact DiscordBans");
        }
    }

    /**
     * @param id - The id to report.
     * @param type - The type (USER or GUILD).
     * @param reason - The reason for reporting.
     * @param guild - The guild where this report is originating (null for none).
     * @return BanReport instance of this report.
     * @throws DiscordBansException - If any error occurs while contacting DiscordBans.
     */
    public BanReport addReport(String id, Type type, String reason, Long guild) throws DiscordBansException {
        return addReport(id, type, reason, guild, null);
    }

    /**
     * @param id - The id to report.
     * @param type - The type (USER or GUILD).
     * @param reason - The reason for reporting.
     * @param submitter - The user id of the person who is submitting this report. (null for default)
     * @return BanReport instance of this report.
     * @throws DiscordBansException - If any error occurs while contacting DiscordBans.
     */
    public BanReport addReport(String id, Type type, String reason, BanUser submitter) throws DiscordBansException {
        return addReport(id, type, reason, null, submitter);
    }

    /**
     * @param id - The id to report.
     * @param type - The type (USER or GUILD).
     * @param reason - The reason for reporting.
     * @return BanReport instance of this report.
     * @throws DiscordBansException - If any error occurs while contacting DiscordBans.
     */
    public BanReport addReport(String id, Type type, String reason) throws DiscordBansException {
        return addReport(id, type, reason, null, null);
    }

    /**
     * @param id - The id to check.
     * @param type - User or Guild
     * @return true if this user/guild is considered banned.
     * @throws DiscordBansException - If any error occurs while contacting DiscordBans.
     */
    public Boolean isBanned(String id, Type type) throws DiscordBansException {
        try {
            HttpResponse<JsonNode> resp = Unirest.get("https://api.discordbans.com/isBanned")
                    .header("API", this.apiKey)
                    .queryString("id", id)
                    .queryString("type", type.name()).asJson();
            if(resp.getBody().getObject().has("error")) {
                throw new DiscordBansException(resp.getBody().getObject().getString("error"));
            }
            JSONObject object = resp.getBody().getObject();
            return object.getBoolean("banned");
        } catch (UnirestException e) {
            throw new DiscordBansException("Failed to contact DiscordBans");
        }
    }

    /**
     * @param uuid - The uuid of the report.
     * @param content - The content of the comment.
     * @return List<BanComment> with all comments for this report (including the new one).
     * @throws DiscordBansException - If any error occurs while contacting DiscordBans.
     */
    public List<BanComment> addComment(String uuid, String content) throws DiscordBansException {
        try {
            HttpResponse<JsonNode> resp = Unirest.get("https://api.discordbans.com/addComment")
                    .header("API", this.apiKey)
                    .queryString("uuid", uuid)
                    .queryString("content", content).asJson();
            if(resp.getBody().getObject().has("error")) {
                throw new DiscordBansException(resp.getBody().getObject().getString("error"));
            }
            return getComments(uuid);
        } catch (UnirestException e) {
            throw new DiscordBansException("Failed to contact DiscordBans");
        }
    }
    
}
