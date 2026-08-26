package com.mp.be.database.enumerator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum NodeStatus {
    RUNNING("running"),
    OFF("off"),
    IDLE("idle"),
    MAINTENANCE("maintenance");

    private final String value;

    NodeStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static NodeStatus fromValue(String text) {
        if (text == null || text.trim().isEmpty()) return null;
        String clean = text.trim();
        for (NodeStatus status : NodeStatus.values()) {
            if (status.value.equalsIgnoreCase(clean) || status.name().equalsIgnoreCase(clean.replace("-", "_"))) {
                return status;
            }
        }
        return null;
    }
}
