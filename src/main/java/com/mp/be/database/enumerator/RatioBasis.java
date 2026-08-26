package com.mp.be.database.enumerator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RatioBasis {
    SHARE("share"),
    PER_UNIT("per_unit"),
    PERCENTAGE("percentage"),
    FIXED_QUANTITY("fixed_quantity");

    private final String value;

    RatioBasis(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static RatioBasis fromValue(String text) {
        if (text == null || text.trim().isEmpty()) return null;
        String clean = text.trim();
        for (RatioBasis b : RatioBasis.values()) {
            if (b.value.equalsIgnoreCase(clean) || b.name().equalsIgnoreCase(clean.replace("-", "_"))) {
                return b;
            }
        }
        return null;
    }
}
