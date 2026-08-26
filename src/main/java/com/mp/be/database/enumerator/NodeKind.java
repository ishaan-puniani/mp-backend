package com.mp.be.database.enumerator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum NodeKind {
    RAW_WAREHOUSE("raw-warehouse"),
    GODOWN("godown"),
    MACHINE("machine"),
    QC_INSPECTION("qc-inspection"),
    WASTAGE_HOLDER("wastage-holder"),
    SCRAP_HOLDER("scrap-holder"),
    GARBAGE_HOLDER("garbage-holder"),
    DISTRIBUTION_WAREHOUSE("distribution-warehouse");

    private final String value;

    NodeKind(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static NodeKind fromValue(String text) {
        if (text == null || text.trim().isEmpty()) return null;
        String clean = text.trim();
        for (NodeKind kind : NodeKind.values()) {
            if (kind.value.equalsIgnoreCase(clean) || kind.name().equalsIgnoreCase(clean.replace("-", "_"))) {
                return kind;
            }
        }
        return null;
    }
}
