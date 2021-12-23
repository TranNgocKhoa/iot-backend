package com.khoa.iot.backend.service;

import com.khoa.iot.backend.entity.AirDeviceData;
import com.khoa.iot.backend.enums.DefaultErrorType;
import com.khoa.iot.backend.exception.ApiRuntimeException;
import com.khoa.iot.backend.model.AirDataItem;
import com.khoa.iot.backend.model.AirTimeSeriesData;
import com.khoa.iot.backend.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceService {
    private final DeviceRepository deviceRepository;
    private final DeviceTimeSeriesServiceSupplier deviceTimeSeriesServiceSupplier;

    public boolean saveDeviceData(AirDeviceData airDeviceData) {
        if (Objects.isNull(airDeviceData)) {
            throw new ApiRuntimeException(DefaultErrorType.HTTP_400);
        }
        try {
            deviceRepository.save(airDeviceData);
        } catch (RuntimeException e) {
            log.error("Error when save => {}", airDeviceData);
            throw e;
        }

        return true;
    }

    public AirTimeSeriesData getTimeSeriesData(String deviceId, Instant startTime, Instant endTime) {
        List<AirDeviceData> airDeviceDataList = deviceTimeSeriesServiceSupplier.get(deviceId, startTime, endTime);

        if (airDeviceDataList.isEmpty()) {
            return new AirTimeSeriesData();
        }

        List<AirDataItem> airDataItems = airDeviceDataList.stream()
                .map(AirDataItem::new)
                .collect(Collectors.toList());

        return airDeviceDataList.stream()
                .findFirst()
                .map(airPingData -> new AirTimeSeriesData(airPingData, airDataItems))
                .orElse(new AirTimeSeriesData());
    }
}
