package com.seailz.discordjar.action.guild.members;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.guild.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class RequestGuildMembersAction {

    private final String guildId;
    /**
     * The limit of users to request.
     * Leave null to request all users.
     */
    private int limit = 0;
    /**
     * The query to search for members.
     * Leave this empty to get all members.
     */
    private String query = "";
    /**
     * If the Member objects should return presences.
     * Requires the <b>GUILD_PRESENCES</b> intent.
     */
    private boolean presences;
    /**
     * List of user ids you'd like to request.
     */
    private List<String> userIds = new ArrayList<>();
    /**
     *  A random nonce is generated by discord.jar to identify the request.
     */
    private final String nonce = String.valueOf(new Random().nextInt(10000000));
    private final DiscordJar discordJar;

    public RequestGuildMembersAction(String guildId, DiscordJar discordJar) {
        this.guildId = guildId;
        this.discordJar = discordJar;
    }

    public RequestGuildMembersAction setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public RequestGuildMembersAction setQuery(String query) {
        this.query = query;
        return this;
    }

    public RequestGuildMembersAction setPresences(boolean presences) {
        this.presences = presences;
        return this;
    }

    public RequestGuildMembersAction setUserIds(List<String> userIds) {
        this.userIds = userIds;
        return this;
    }

    public String getGuildId() {
        return guildId;
    }

    public int getLimit() {
        return limit;
    }

    public String getQuery() {
        return query;
    }

    public boolean isPresences() {
        return presences;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public String getNonce() {
        return nonce;
    }

    public CompletableFuture<List<Member>> run() {
        CompletableFuture<List<Member>> future = new CompletableFuture<>();
        discordJar.getGateway().requestGuildMembers(this, future);
        return future;
    }


}