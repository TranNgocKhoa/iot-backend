package com.khoa.iot.backend.service;

import com.khoa.iot.backend.entity.AirDeviceData;
import com.khoa.iot.backend.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceTimeSeriesServiceSupplier {
    private final DeviceRepository deviceRepository;

    public List<AirDeviceData> get(String deviceId, Instant startTime, Instant endTime) {
        if (startTime == null && endTime == null) {
            return deviceRepository.findAllByDeviceId(deviceId);
        } else if (startTime == null) {
            return deviceRepository.findAllByDeviceIdAndTimestampBefore(deviceId, endTime);
        } else if (endTime == null) {
            return deviceRepository.findAllByDeviceIdAndTimestampAfter(deviceId, startTime);
        } else {
            return deviceRepository.findAllByDeviceIdAndTimestampBetween(deviceId, startTime, endTime);
        }
    }
}
