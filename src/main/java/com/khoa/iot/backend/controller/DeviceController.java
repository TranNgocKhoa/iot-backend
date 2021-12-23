package com.khoa.iot.backend.controller;

import com.khoa.iot.backend.entity.AirDeviceData;
import com.khoa.iot.backend.model.AirTimeSeriesData;
import com.khoa.iot.backend.service.DeviceService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {
    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public boolean saveDeviceData(@RequestBody AirDeviceData airDeviceData) {
        return deviceService.saveDeviceData(airDeviceData);
    }

    @GetMapping("/{deviceId}")
    public AirTimeSeriesData getTimeSeriesData(@PathVariable String deviceId,
                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startTime,
                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endTime) {
        return deviceService.getTimeSeriesData(deviceId, startTime, endTime);
    }

}
