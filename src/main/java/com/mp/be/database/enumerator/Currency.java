package com.mp.be.database.enumerator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Currency {
    INR("INR", "₹", "Indian Rupee"),
    USD("USD", "$", "US Dollar"),
    EUR("EUR", "€", "Euro"),
    GBP("GBP", "£", "British Pound"),
    AED("AED", "د.إ", "UAE Dirham"),
    SGD("SGD", "S$", "Singapore Dollar"),
    AUD("AUD", "A$", "Australian Dollar"),
    CAD("CAD", "C$", "Canadian Dollar"),
    JPY("JPY", "¥", "Japanese Yen"),
    CNY("CNY", "¥", "Chinese Yuan");

    private final String code;
    private final String symbol;
    private final String name;

    Currency(String code, String symbol, String name) {
        this.code = code;
        this.symbol = symbol;
        this.name = name;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    @JsonCreator
    public static Currency fromValue(String text) {
        if (text == null || text.trim().isEmpty()) return null;
        String clean = text.trim();
        for (Currency c : Currency.values()) {
            if (c.code.equalsIgnoreCase(clean) || c.name.equalsIgnoreCase(clean) || c.symbol.equals(clean)) {
                return c;
            }
        }
        return null;
    }
}
