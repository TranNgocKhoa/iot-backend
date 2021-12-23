package com.khoa.iot.backend.model;

import com.khoa.iot.backend.entity.AirDeviceData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class AirTimeSeriesData extends AbstractDeviceData {
    private List<AirDataItem> data;

    public AirTimeSeriesData(@NonNull AirDeviceData airDeviceData, List<AirDataItem> airDataItems) {
        this.deviceId = airDeviceData.getDeviceId();
        this.latitude = airDeviceData.getLatitude();
        this.longitude = airDeviceData.getLongitude();
        this.data = new ArrayList<>(airDataItems);
    }

    public List<AirDataItem> getData() {
        if (Objects.isNull(data)) {
            return new ArrayList<>();
        }
        return data;
    }
}
