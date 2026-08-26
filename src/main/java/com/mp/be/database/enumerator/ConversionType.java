package com.mp.be.database.enumerator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ConversionType {
    IDENTITY("identity"),
    DIVIDE_BY_PROPERTY("divideByTargetProperty"),
    MULTIPLY_BY_PROPERTY("multiplyByTargetProperty"),
    DENSITY("density"),
    AIR_OVERRUN("airOverrun"),
    CUSTOM_FORMULA("customFormula");

    private final String value;

    ConversionType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ConversionType fromValue(String text) {
        if (text == null || text.trim().isEmpty()) return null;
        String clean = text.trim();
        for (ConversionType type : ConversionType.values()) {
            if (type.value.equalsIgnoreCase(clean) || type.name().equalsIgnoreCase(clean.replace("-", "_"))) {
                return type;
            }
        }
        return null;
    }
}
