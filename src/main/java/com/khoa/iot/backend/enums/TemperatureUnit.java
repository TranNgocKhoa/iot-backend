package com.khoa.iot.backend.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TemperatureUnit {
    CELSIUS("C");

    private final String shortNotion;

    TemperatureUnit(String shortValue) {
        this.shortNotion = shortValue;
    }

    @JsonValue
    public String getShortNotion() {
        return shortNotion;
    }
}
