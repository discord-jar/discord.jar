package com.seailz.discordjar.action.sku;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.monetization.Entitlement;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.utils.rest.DiscordResponse;
import com.seailz.discordjar.utils.rest.Response;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListEntitlementRequest {

    private final DiscordJar jar;
    private String userId;
    private List<String> skuIds;
    private String before;
    private String after;
    private int limit = 100;
    private String guildId;
    private boolean excludeEnded = false;

    public ListEntitlementRequest(DiscordJar jar) {
        this.jar = jar;
    }

    /**
     * Sets the user ID to get entitlements for.
     */
    public ListEntitlementRequest setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    /**
     * Sets the SKU IDs to get entitlements for.
     */
    public ListEntitlementRequest setSkuIds(List<String> skuIds) {
        this.skuIds = skuIds;
        return this;
    }

    public ListEntitlementRequest addSkuId(String skuId) {
        if (this.skuIds == null) this.skuIds = new ArrayList<>();
        this.skuIds.add(skuId);
        return this;
    }

    /**
     * Limits entitlements to before this ID.
     */
    public ListEntitlementRequest setBefore(String before) {
        this.before = before;
        return this;
    }

    /**
     * Limits entitlements to after this ID.
     */
    public ListEntitlementRequest setAfter(String after) {
        this.after = after;
        return this;
    }

    /**
     * Limits entitlements to this amount. 1-100
     */
    public ListEntitlementRequest setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    /**
     * Limits entitlements to this guild ID.
     */
    public ListEntitlementRequest setGuildId(String guildId) {
        this.guildId = guildId;
        return this;
    }

    /**
     * Whether or not to exclude ended entitlements.
     */
    public ListEntitlementRequest setExcludeEnded(boolean excludeEnded) {
        this.excludeEnded = excludeEnded;
        return this;
    }

    public Response<List<Entitlement>> run() {
        Response<List<Entitlement>> response = new Response<>();
        if (limit < 1 || limit > 100) throw new IllegalArgumentException("Limit must be between 1 and 100");
        new Thread(() -> {
            String urlWithQuery = URLS.GET.APPLICATION.LIST_ENTITLEMENTS;
            urlWithQuery += "?";
            if (userId != null) urlWithQuery += "user_id=" + userId + "&";
            if (skuIds != null) {
                StringBuilder skuIdsString = new StringBuilder();
                for (String skuId : skuIds) {
                   skuIdsString.append(skuId).append(",");
                }
                skuIdsString = new StringBuilder(skuIdsString.substring(0, skuIdsString.length() - 1));
                urlWithQuery += "sku_ids=" + skuIdsString + "&";
            }
            if (before != null) urlWithQuery += "before=" + before + "&";
            if (after != null) urlWithQuery += "after=" + after + "&";
            urlWithQuery += "limit=" + limit + "&";
            if (guildId != null) urlWithQuery += "guild_id=" + guildId + "&";
            urlWithQuery += "exclude_ended=" + excludeEnded;

            DiscordResponse req = null;
            try {
                req = new DiscordRequest(
                        new JSONObject(),
                        new HashMap<>(),
                        urlWithQuery.replace("{application.id}", jar.getSelfInfo().id()),
                        jar,
                        URLS.GET.APPLICATION.LIST_ENTITLEMENTS,
                        RequestMethod.GET
                ).invoke();
            } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
                response.completeError(new Response.Error(e));
                return;
            }

            List<Entitlement> entitlements = new ArrayList<>();
            for (Object o : req.arr()) {
                entitlements.add(Entitlement.decompile(jar, (JSONObject) o));
            }
            response.complete(entitlements);
        }).start();
        return response;
    }

}
