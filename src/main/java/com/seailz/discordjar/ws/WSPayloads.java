package com.seailz.discordjar.ws;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Each payload is a JSON object or array that is sent with a set of fields.
 *
 * @author Seailz
 */
public class WSPayloads {

    public static final Payload HEARBEAT = new Payload(
            new JSONObject()
                    .put("op", 1)
                    .put("d", "SLOT HERE")
    ).addParam(new Payload.Param("d"));

    /**
     * Represents a payload that is sent to the gateway.
     *
     * @author Seailz
     */
    public static class Payload {
        private final List<Param> params = new ArrayList<>();
        private final JSONObject payload;

        public Payload(JSONObject obj) {
            this.payload = obj;
        }

        /**
         * Adds a parameter to the payload.
         *
         * @param param The parameter to add.
         * @return The payload.
         */
        public Payload addParam(Param param) {
            params.add(param);
            return this;
        }

        public JSONObject fill(Object... params) {
            JSONObject obj = payload;
            for (int i = 0; i < params.length; i++) {
                obj = obj.put(this.params.get(i).slot, params[i]);
            }
            return obj;
        }

        /**
         * Parameters of a payload. The slot is the place that the parameter is in the payload, for example, if the payload is:
         * <pre>
         *     {
         *         "op": 1,
         *         "d": "SLOT HERE"
         *     }
         * </pre>
         * <p>
         * Then the slot would be "d".
         * Or, if the payload is:
         * <pre>
         *     {
         *         "op": 1,
         *         "d": {
         *              "field_1": "SLOT HERE"
         *         }
         *     }
         * </pre>
         * <p>
         * Then the slot would be "d.field_1".
         *
         * @author Seailz
         */
        public static class Param {
            private final String slot;

            public Param(String slot) {
                this.slot = slot;
            }
        }
    }

}
