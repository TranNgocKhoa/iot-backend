package com.khoa.iot.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirData {
    protected int humidity;
    protected TemperatureData temperature;
}
