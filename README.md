<img align="right" src="https://avatars0.githubusercontent.com/u/25252290?s=250" height="250" height="250">

# Discord Bans
Java API for Discord Bans

## What is Discord Bans?
Discord Bans is a centralized Discord ban management system.

## Current Status
The service is up and running, however, this API is under reconstruction.

Below an example on how to currently post data to our services.

```java
public class DiscordBansListener extends ListenerAdapter {

    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    public void onGuildBan(GuildBanEvent event) {
        AuditLogEntry auditLog = null;
        List<AuditLogEntry> auditLogEntryList;
        try {
            auditLogEntryList = event.getGuild().getAuditLogs().type(ActionType.BAN).complete();
        } catch (PermissionException | ErrorResponseException ex) {
            return;
        }
        for(AuditLogEntry entry: auditLogEntryList) {
            if(entry.getTargetIdLong() != event.getUser().getIdLong()) {
                continue;
            }
            auditLog = entry;
            break;
        }
        if(auditLog == null || auditLog.getReason() == null || event.getUser().isBot()) return;
        // --
        AuditLogEntry finalAuditLog = auditLog;
        Guild guild = event.getGuild();
        User admin = finalAuditLog.getUser();
        User victim = event.getUser();
        new Thread(() -> { // ASYNC running is advised (Please use a proper ThreadPool)
            JSONObject rawBody = new JSONObject();
            rawBody.put("reason", finalAuditLog.getReason());
            rawBody.put("user", new JSONObject().put("id", victim.getIdLong()).put("name", victim.getName()));
            rawBody.put("admin", new JSONObject().put("id", admin.getIdLong()).put("name", admin.getName()));
            rawBody.put("guild", new JSONObject().put("id", guild.getIdLong()).put("name", guild.getName()));

            RequestBody body = RequestBody.create(JSON, rawBody.toString());
            Request request = new Request.Builder()
                    .url("https://api.discordbans.com/bans")
                    .addHeader("Authorization", ur_token_here)
                    .post(body).build();
            try {
                getOkHttp().newCall(request).execute().close(); // OkHttp call
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
```
