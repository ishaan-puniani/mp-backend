package com.mp.be.database.enumerator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MeasurementUnit {
    // Volume
    L("L", "Liter"),
    ML("ml", "Milliliter"),
    GAL("gal", "Gallon"),
    KL("kL", "Kiloliter"),
    M3("m3", "Cubic Meter"),

    // Mass / Weight
    KG("kg", "Kilogram"),
    G("g", "Gram"),
    MG("mg", "Milligram"),
    TON("ton", "Metric Ton"),
    LB("lb", "Pound"),
    OZ("oz", "Ounce"),

    // Discrete / Packaging Counts
    PCS("pcs", "Pieces"),
    CARTON("carton", "Carton"),
    BOX("box", "Box"),
    BAG("bag", "Bag"),
    PACK("pack", "Pack"),
    DRUM("drum", "Drum"),
    PALLET("pallet", "Pallet"),
    BATCH("batch", "Batch"),

    // Flow / Rate Units
    L_PER_MIN("L/min", "Liters per minute"),
    L_PER_HR("L/hr", "Liters per hour"),
    PCS_PER_MIN("pcs/min", "Pieces per minute"),
    PCS_PER_HR("pcs/hr", "Pieces per hour"),
    KG_PER_HR("kg/hr", "Kilograms per hour"),

    // Ratios & Percentages
    PERCENT("%", "Percentage"),
    SHARE("share", "Share Ratio (0 to 1)"),
    PPM("ppm", "Parts Per Million"),

    // Time / Duration
    SEC("s", "Second"),
    MIN("min", "Minute"),
    HR("hr", "Hour"),
    DAY("day", "Day"),
    MONTH("month", "Month");

    private final String value;
    private final String description;

    MeasurementUnit(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static MeasurementUnit fromValue(String text) {
        if (text == null || text.trim().isEmpty()) return null;
        String clean = text.trim();
        for (MeasurementUnit unit : MeasurementUnit.values()) {
            if (unit.value.equalsIgnoreCase(clean) || unit.name().equalsIgnoreCase(clean.replace("/", "_PER_").replace("%", "PERCENT"))) {
                return unit;
            }
        }
        return null;
    }
}
