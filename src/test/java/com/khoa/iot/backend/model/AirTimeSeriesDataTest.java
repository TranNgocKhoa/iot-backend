package com.khoa.iot.backend.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AirTimeSeriesDataTest {

    @Test
    void getData_shouldNotReturnNull() {
        AirTimeSeriesData airTimeSeriesData = new AirTimeSeriesData();

        assertNotNull(airTimeSeriesData.getData());

        airTimeSeriesData.setData(new ArrayList<>());
        assertNotNull(airTimeSeriesData.getData());
    }
}