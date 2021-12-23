package com.khoa.iot.backend.model;

import com.khoa.iot.backend.entity.AirDeviceData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
public class AirDataItem extends AirData {
    private Instant timestamp;

    public AirDataItem(AirData airData) {
        super(airData.getHumidity(), airData.getTemperature());
    }

    public AirDataItem(AirDeviceData airDeviceData) {
        this(airDeviceData.getData());
        this.timestamp = airDeviceData.getTimestamp();
    }
}
