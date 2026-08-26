package com.mp.be.database.enumerator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MaterialType {
    RAW_MATERIAL("raw-material"),
    INTERMEDIATE_WIP("intermediate-wip"),
    FINISHED_GOODS("finished-goods"),
    WASTE("waste"),
    SCRAP("scrap"),
    PACKAGING("packaging"),
    CONSUMABLE("consumable"),
    CHEMICAL("chemical");

    private final String value;

    MaterialType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static MaterialType fromValue(String text) {
        if (text == null || text.trim().isEmpty()) return null;
        String clean = text.trim();
        for (MaterialType type : MaterialType.values()) {
            if (type.value.equalsIgnoreCase(clean) || type.name().equalsIgnoreCase(clean.replace("-", "_"))) {
                return type;
            }
        }
        return null;
    }
}
