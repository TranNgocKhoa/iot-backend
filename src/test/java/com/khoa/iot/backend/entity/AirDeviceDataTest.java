package com.khoa.iot.backend.entity;

import com.khoa.iot.backend.model.AirData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AirDeviceDataTest {

    @Test
    void getData_shouldNotReturnNull() {
        AirDeviceData airDeviceData = new AirDeviceData();
        airDeviceData.setData(null);

        assertNotNull(airDeviceData.getData());

        airDeviceData.setData(new AirData());

        assertNotNull(airDeviceData.getData());
    }
}