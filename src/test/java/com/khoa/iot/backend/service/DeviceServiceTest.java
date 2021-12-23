package com.khoa.iot.backend.service;

import com.khoa.iot.backend.entity.AirDeviceData;
import com.khoa.iot.backend.exception.ApiRuntimeException;
import com.khoa.iot.backend.model.AirTimeSeriesData;
import com.khoa.iot.backend.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class DeviceServiceTest {
    @Mock
    DeviceRepository deviceRepository;
    @Mock
    DeviceTimeSeriesServiceSupplier deviceTimeSeriesServiceSupplier;

    @InjectMocks
    DeviceService deviceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenNull_saveAirDeviceData_shouldThrowApiRunTimeException() {
        assertThrows(ApiRuntimeException.class, () -> deviceService.saveDeviceData(null));
    }

    @Test
    void givenValidInput_andRepositorySuccess_saveAirDeviceData_shouldReturnTrue() {
        when(deviceRepository.save(any())).thenReturn(null);

        assertTrue(deviceService.saveDeviceData(new AirDeviceData()));
    }

    @Test
    void givenValidInput_andRepositoryFail_saveAirDeviceData_shouldThrowExceptionAndWriteLog() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        when(deviceRepository.save(any())).thenThrow(new RuntimeException("Error Save"));

        assertThrows(RuntimeException.class, () -> deviceService.saveDeviceData(new AirDeviceData()));
        assertTrue(outContent.toString().contains("Error when save =>"));
    }

    @Test
    void givenRepositoryReturnEmptyList_getTimeSeriesData_shouldReturnObjectWithEmptyData() {
        when(deviceRepository.findAllByDeviceIdAndTimestampBetween(any(), any(), any())).thenReturn(new ArrayList<>());

        AirTimeSeriesData timeSeriesData = deviceService.getTimeSeriesData("RANDOM_DEVICE", Instant.now(), Instant.now());

        assertTrue(timeSeriesData.getData().isEmpty());
    }

    @Test
    void givenRepositoryReturnList_getTimeSeriesData_shouldReturnObjectWithDataHasSameSize() {
        List<AirDeviceData> airDeviceData = Arrays.asList(
                new AirDeviceData(),
                new AirDeviceData()
        );
        when(deviceTimeSeriesServiceSupplier.get(any(), any(), any())).thenReturn(airDeviceData);

        AirTimeSeriesData timeSeriesData = deviceService.getTimeSeriesData("RANDOM_DEVICE", Instant.now(), Instant.now());

        assertEquals(timeSeriesData.getData().size(), airDeviceData.size());
    }

    @Test
    void givenRepositoryReturnList_getTimeSeriesData_shouldReturnObjectHasCorrectDeviceData() {
        List<AirDeviceData> airDeviceDataList = new ArrayList<>();

        AirDeviceData airDeviceData = new AirDeviceData();
        airDeviceData.setDeviceId("Device1");
        airDeviceData.setLatitude(1);
        airDeviceData.setLongitude(2);
        airDeviceDataList.add(airDeviceData);

        AirDeviceData otherAirDeviceData = new AirDeviceData();
        otherAirDeviceData.setDeviceId("Device1");
        otherAirDeviceData.setLatitude(1);
        otherAirDeviceData.setLongitude(2);
        airDeviceDataList.add(otherAirDeviceData);

        when(deviceTimeSeriesServiceSupplier.get(any(), any(), any())).thenReturn(airDeviceDataList);

        AirTimeSeriesData timeSeriesData = deviceService.getTimeSeriesData("RANDOM_DEVICE", Instant.now(), Instant.now());

        assertEquals(timeSeriesData.getData().size(), airDeviceDataList.size());
        assertEquals(timeSeriesData.getDeviceId(), airDeviceData.getDeviceId());
        assertEquals(timeSeriesData.getLatitude(), airDeviceData.getLatitude());
        assertEquals(timeSeriesData.getLongitude(), airDeviceData.getLongitude());
    }
}