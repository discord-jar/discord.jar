package com.seailz.discordjar.model.scopes;

public enum Scope {

    IDENTIFY,
    GUILDS,
    GDM__JOIN,
    RPC__VOICE__READ,
    BOT,
    APPLICATIONS__BUILD__UPLOAD,
    APPLICATIONS__STORE__UPDATE,
    ACTIVITIES__WRITE,
    DM_CHANNELS__READ,
    APPLICATIONS__COMMANDS__PERMISSIONS__UPDATE,
    EMAIL,
    GUILDS__JOIN,
    RPC,
    RPC__VOICE__WRITE,
    WEBHOOK__INCOMING,
    APPLICATIONS__BUILD__READ,
    APPLICATIONS__ENTITLEMENTS,
    RELATIONSHIPS__READ,
    ROLE_CONNECTIONS__WRITE,
    CONNECTIONS,
    GUILD__MEMBERS__READ,
    RPC__NOTIFICATIONS__READ,
    RPC__ACTIVITIES__WRITE,
    MESSAGES__READ,
    APPLICATION__COMMANDS,
    ACTIVITIES__READ,
    VOICE,

    UNKNOWN;

    private String scope;

    Scope() {
        this.scope = name().toLowerCase().replaceAll("__", ".");
    }

    public static Scope fromString(String scope) {
        for (Scope value : values()) {
            if (value.scope.equalsIgnoreCase(scope)) {
                return value;
            }
        }
        return ofUnknown(scope);
    }

    public static Scope ofUnknown(String scope) {
        Scope s = UNKNOWN;
        s.scope = scope;
        return s;
    }

    public String getScope() {
        return scope;
    }

}
