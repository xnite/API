package com.discordbans;

import com.discordbans.entities.*;
import com.discordbans.entities.enums.Severity;
import com.discordbans.entities.enums.State;
import com.discordbans.entities.enums.Type;
import com.discordbans.exceptions.DiscordBansException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.body.MultipartBody;
import net.jodah.expiringmap.ExpiringMap;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (C) DiscordBans 2017
 * This project is licensed under the GNU GPLv3 License.
 */
@SuppressWarnings("WeakerAccess")
public class DiscordBans implements DiscordBansImpl {
    
    private String apiKey;
    private boolean usingCache;
    
    private Map<String, BanReport> reportCache;
    private Map<String, Boolean> banCache;
    private Map<String, BanGuild> guildCache;
    private Map<String, BanUser> userCache;
    private Map<String, BanComment> commentCache;
    
    protected DiscordBans(String apiKey, Boolean cache, TimeUnit timeUnit, Long time) {
        this.apiKey = apiKey;
        usingCache = cache;
        if(cache) {
            reportCache = ExpiringMap.builder().expiration(time, timeUnit).build();
            banCache = ExpiringMap.builder().expiration(time, timeUnit).build();
            guildCache = ExpiringMap.builder().expiration(time, timeUnit).build();
            userCache = ExpiringMap.builder().expiration(time, timeUnit).build();
            commentCache = ExpiringMap.builder().expiration(time, timeUnit).build();
        }
    }
    
    public static DBansBuilder builder(String apiKey) {
        return new DBansBuilder(apiKey);
    }
    
    @Override
    public BanReport getReport(String uuid) throws DiscordBansException {
        if(usingCache && this.reportCache.containsKey(uuid)) {
            return this.reportCache.get(uuid);
        }
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
            BanReport report = new BanReport(uuid, reported, type, submitter, state, admin, severity, reason, guild, timestamp); 
            if(usingCache) {
                this.reportCache.put(uuid, report);
            }
            return report;
        } catch (UnirestException e) {
            throw new DiscordBansException("Failed to contact DiscordBans");
        }
    }
    
    @Override
    public BanGuild getGuild(String id) throws DiscordBansException {
        if(usingCache && this.guildCache.containsKey(id)) {
            return this.guildCache.get(id);
        }
        try {
            HttpResponse<JsonNode> resp = Unirest.get("https://api.discordbans.com/getGuild")
                    .header("API", this.apiKey)
                    .queryString("id", id).asJson();
            if(resp.getBody().getObject().has("error")) {
                throw new DiscordBansException(resp.getBody().getObject().getString("error"));
            }
            JSONObject object = resp.getBody().getObject();
            BanGuild banGuild = new BanGuild(object.getString("id"), object.getString("name"), object.getDouble("reputation"));
            if(usingCache) {
                this.guildCache.put(banGuild.getId(), banGuild);
            }
            return banGuild;
        } catch (UnirestException e) {
            throw new DiscordBansException("Failed to contact DiscordBans");
        }
    }
    
    @Override
    public BanUser getUser(String id) throws DiscordBansException {
        if(usingCache && this.userCache.containsKey(id)) {
            return this.userCache.get(id);
        }
        try {
            HttpResponse<JsonNode> resp = Unirest.get("https://api.discordbans.com/getUser")
                    .header("API", this.apiKey)
                    .queryString("id", id).asJson();
            if(resp.getBody().getObject().has("error")) {
                throw new DiscordBansException(resp.getBody().getObject().getString("error"));
            }
            JSONObject object = resp.getBody().getObject();
            BanUser banUser = new BanUser(object.getString("id"), object.getBoolean("admin"), object.getString("username"), object.getLong("discriminator"));
            if(usingCache) {
                this.userCache.put(banUser.getId(), banUser);
            }
            return banUser;
        } catch (UnirestException e) {
            throw new DiscordBansException("Failed to contact DiscordBans");
        }
    }

    @Override
    public BanComment getComment(String uuid) throws DiscordBansException {
        if(usingCache && this.commentCache.containsKey(uuid)) {
            return this.commentCache.get(uuid);
        }
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
            BanComment comment = new BanComment(submitter, report, uuid, content, Timestamp.valueOf(rawstamp));
            if(usingCache) {
                this.commentCache.put(comment.getUUID(), comment);
            }
            return comment;
        } catch (UnirestException e) {
            throw new DiscordBansException("Failed to contact DiscordBans");
        }
    }
    
    @Override
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
                if(usingCache && this.commentCache.containsKey(comment.getString("uuid"))) {
                    comments.add(this.commentCache.get(comment.getString("uuid")));
                    continue;
                }
                Long submitter = comment.getLong("submitter");
                String content = comment.getString("content");
                String rawstamp = comment.getString("timestamp");
                BanComment comment1 = new BanComment(submitter, uuid, comment.getString("uuid"), content, Timestamp.valueOf(rawstamp));
                if(usingCache) {
                    this.commentCache.put(comment1.getUUID(), comment1);
                }
                comments.add(comment1);
            }
        } catch (UnirestException ex) {
            throw new DiscordBansException("Failed to contact DiscordBans");
        }
        return comments;
    }

    @Override
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
                if(usingCache && this.reportCache.containsKey(uuid)) {
                    reports.add(this.reportCache.get(uuid));
                    continue;
                }
                Severity severity = Severity.fromString(report.getString("severity"));
                Long submitter = report.has("submitter") ? report.getLong("submitter") : 250758034551341056L;
                String reason = report.getString("reason");
                Long guild = report.getLong("guild");
                Long admin = report.getLong("admin");
                State state = State.fromString(report.getString("state"));
                Timestamp timestamp = Timestamp.valueOf(report.getString("timestamp"));
                BanReport report1 = new BanReport(uuid, Long.valueOf(id), type, submitter, state, admin, severity, reason, guild, timestamp);
                if(usingCache) {
                    this.reportCache.put(uuid, report1);
                }
                reports.add(report1);
            }
        } catch (UnirestException ex) {
            throw new DiscordBansException("Failed to contact DiscordBans");
        }
        return reports;
    }
    
    @Override
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
            BanReport report = new BanReport(object.getString("uuid"), reported, type, s, state, admin, severity, reason, guild, timestamp);
            if(usingCache) {
                this.reportCache.put(object.getString("uuid"), report);
            }
            return report;
        } catch (UnirestException e) {
            throw new DiscordBansException("Failed to contact DiscordBans");
        }
    }

    @Override
    public BanReport addReport(String id, Type type, String reason, Long guild) throws DiscordBansException {
        return addReport(id, type, reason, guild, null);
    }
    
    @Override
    public BanReport addReport(String id, Type type, String reason, BanUser submitter) throws DiscordBansException {
        return addReport(id, type, reason, null, submitter);
    }

    @Override
    public BanReport addReport(String id, Type type, String reason) throws DiscordBansException {
        return addReport(id, type, reason, null, null);
    }
    
    @Override
    public Boolean isBanned(String id, Type type) throws DiscordBansException {
        if(usingCache && this.banCache.containsKey(id + "/" + type.name())) {
            return this.banCache.get(id + "/" + type.name());
        }
        try {
            HttpResponse<JsonNode> resp = Unirest.get("https://api.discordbans.com/isBanned")
                    .header("API", this.apiKey)
                    .queryString("id", id)
                    .queryString("type", type.name()).asJson();
            if(resp.getBody().getObject().has("error")) {
                throw new DiscordBansException(resp.getBody().getObject().getString("error"));
            }
            JSONObject object = resp.getBody().getObject();
            if(usingCache) {
                this.banCache.put(id + "/" + type.name(), object.getBoolean("banned"));
            }
            return object.getBoolean("banned");
        } catch (UnirestException e) {
            throw new DiscordBansException("Failed to contact DiscordBans");
        }
    }
    
    @Override
    public List<BanComment> addComment(String uuid, String content) throws DiscordBansException {
        try {
            HttpResponse<JsonNode> resp = Unirest.post("https://api.discordbans.com/addComment")
                    .header("API", this.apiKey)
                    .field("uuid", uuid)
                    .field("content", content).asJson();
            if(resp.getBody().getObject().has("error")) {
                throw new DiscordBansException(resp.getBody().getObject().getString("error"));
            }
            return getComments(uuid);
        } catch (UnirestException e) {
            throw new DiscordBansException("Failed to contact DiscordBans");
        }
    }
    
}
