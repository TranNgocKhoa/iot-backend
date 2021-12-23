package com.khoa.iot.backend.entity;

import com.khoa.iot.backend.model.AbstractDeviceData;
import com.khoa.iot.backend.model.AirData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Objects;

@Document(collection = "air_device_data")
@Data
@EqualsAndHashCode(callSuper = true)
public class AirDeviceData extends AbstractDeviceData {
    private AirData data;
    @CreatedDate
    @Field(write = Field.Write.NON_NULL)
    private Instant timestamp;

    public AirData getData() {
        if (Objects.isNull(data)) {
            return new AirData();
        }

        return data;
    }
}
