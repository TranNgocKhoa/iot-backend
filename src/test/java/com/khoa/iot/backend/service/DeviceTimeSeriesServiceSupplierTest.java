package com.khoa.iot.backend.service;

import com.khoa.iot.backend.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.Instant;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class DeviceTimeSeriesServiceSupplierTest {

    @Mock
    DeviceRepository deviceRepository;
    @InjectMocks
    DeviceTimeSeriesServiceSupplier deviceTimeSeriesServiceSupplier;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenAllParamNotNull_shouldDelegateToFindByBetween() {
        String deviceId = "1111";
        Instant start = Instant.now();
        Instant end = Instant.now();
        deviceTimeSeriesServiceSupplier.get(deviceId, start, end);

        verify(deviceRepository, times(1)).findAllByDeviceIdAndTimestampBetween(deviceId, start, end);
    }

    @Test
    void givenAllParamNotNullExceptStartTime_shouldDelegateToFindByBefore() {
        String deviceId = "1111";
        Instant start = null;
        Instant end = Instant.now();
        deviceTimeSeriesServiceSupplier.get(deviceId, start, end);

        verify(deviceRepository, times(1)).findAllByDeviceIdAndTimestampBefore(deviceId, end);
    }

    @Test
    void givenAllParamNotNullExceptEndTime_shouldDelegateToFindByAfter() {
        String deviceId = "1111";
        Instant start = Instant.now();
        Instant end = null;
        deviceTimeSeriesServiceSupplier.get(deviceId, start, end);

        verify(deviceRepository, times(1)).findAllByDeviceIdAndTimestampAfter(deviceId, start);
    }

    @Test
    void givenAllParamNotNullExceptEndTimeAndStartTime_shouldDelegateToFindByDeviceId() {
        String deviceId = "1111";
        Instant start = null;
        Instant end = null;
        deviceTimeSeriesServiceSupplier.get(deviceId, start, end);

        verify(deviceRepository, times(1)).findAllByDeviceId(deviceId);
    }
}