package com.mp.be.database.enumerator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ItemStatus {
    DRAFT("draft"),
    ACTIVE("active"),
    INACTIVE("inactive"),
    ARCHIVED("archived");

    private final String value;

    ItemStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ItemStatus fromValue(String text) {
        if (text == null || text.trim().isEmpty()) return null;
        String clean = text.trim();
        for (ItemStatus status : ItemStatus.values()) {
            if (status.value.equalsIgnoreCase(clean) || status.name().equalsIgnoreCase(clean.replace("-", "_"))) {
                return status;
            }
        }
        return null;
    }
}
