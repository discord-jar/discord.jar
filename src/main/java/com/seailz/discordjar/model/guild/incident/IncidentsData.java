package com.seailz.discordjar.model.guild.incident;

import com.seailz.discordjar.core.Compilerable;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.json.JSONObject;

/**
 * Represents an IncidentsData object.
 * <br> Each value is nullable & has a limit of 24 hours into the future.
 */
public record IncidentsData(
        DateTime invitesDisabledUntil,
        DateTime dmsDisabledUntil
) implements Compilerable {

    public boolean areInvitesDisabled() {
        if (invitesDisabledUntil == null) return false;
        return invitesDisabledUntil.isAfterNow();
    }

    public boolean areDmsDisabled() {
        if (dmsDisabledUntil == null) return false;
        return dmsDisabledUntil.isAfterNow();
    }

    @Override
    public JSONObject compile() {
        return new JSONObject()
                .put("invites_disabled_until", invitesDisabledUntil.toString())
                .put("dms_disabled_until", dmsDisabledUntil.toString());
    }

    public static IncidentsData decompile(@NotNull JSONObject obj) {
        DateTime invitesDisabledUntil = obj.has("invites_disabled_until") && !obj.isNull("invites_disabled_until") ? DateTime.parse(obj.getString("invites_disabled_until")) : null;
        DateTime dmsDisabledUntil = obj.has("dms_disabled_until") && !obj.isNull("dms_disabled_until") ? DateTime.parse(obj.getString("dms_disabled_until")) : null;

        return new IncidentsData(
                invitesDisabledUntil,
                dmsDisabledUntil
        );
    }

    public static IncidentsData none() {
        return new IncidentsData(
                null,
                null
        );
    }

    public static IncidentsData invitesDisabled23Hours() {
        return new IncidentsData(
                DateTime.now().plusHours(23),
                null
        );
    }

    public static IncidentsData dmsDisabled23Hours() {
        return new IncidentsData(
                null,
                DateTime.now().plusHours(23)
        );
    }

    public static IncidentsData allDisabled23Hours() {
        return new IncidentsData(
                DateTime.now().plusHours(23),
                DateTime.now().plusHours(23)
        );
    }
}
