package com.mp.be.database.enumerator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EdgeRole {
    PRIMARY_FLOW("primary"),
    INGREDIENT_FLOW("ingredient"),
    WASTAGE_FLOW("wastage"),
    SCRAP_FLOW("scrap"),
    GARBAGE_FLOW("garbage"),
    REWORK_FLOW("rework"),
    BYPRODUCT_FLOW("byproduct"),
    MAIN("main"),
    BYPASS("bypass"),
    RECYCLE("recycle");

    private final String value;

    EdgeRole(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static EdgeRole fromValue(String text) {
        if (text == null || text.trim().isEmpty()) return null;
        String clean = text.trim();
        for (EdgeRole role : EdgeRole.values()) {
            if (role.value.equalsIgnoreCase(clean) || role.name().equalsIgnoreCase(clean.replace("-", "_"))) {
                return role;
            }
        }
        return null;
    }
}
