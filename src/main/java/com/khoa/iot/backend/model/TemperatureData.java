package com.khoa.iot.backend.model;

import com.khoa.iot.backend.enums.TemperatureUnit;
import lombok.Data;

@Data
public class TemperatureData {
    private TemperatureUnit unit;
    private String value;
}
