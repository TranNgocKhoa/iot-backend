package com.khoa.iot.backend.model;

import lombok.Data;

@Data
public abstract class AbstractDeviceData {
    protected String deviceId;
    protected float latitude;
    protected float longitude;
}
