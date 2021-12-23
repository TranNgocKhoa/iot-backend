package com.khoa.iot.backend.repository;

import com.khoa.iot.backend.entity.AirDeviceData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface DeviceRepository extends MongoRepository<AirDeviceData, String> {

    @Query(value = "{ 'deviceId' : ?0 }",
            sort = "{'timestamp': 1 }")
    @NonNull
    List<AirDeviceData> findAllByDeviceId(String deviceId);

    @Query(value = "{ 'deviceId' : ?0, 'timestamp' : { '$gte' : ?1, '$lte' : ?2 }}",
            sort = "{'timestamp': 1 }")
    @NonNull
    List<AirDeviceData> findAllByDeviceIdAndTimestampBetween(String deviceId, Instant startTime, Instant endTime);

    @Query(value = "{ 'deviceId' : ?0, 'timestamp' : { '$gte' : ?1}}",
            sort = "{'timestamp': 1 }")
    @NonNull
    List<AirDeviceData> findAllByDeviceIdAndTimestampAfter(String deviceId, Instant startTime);

    @Query(value = "{ 'deviceId' : ?0, 'timestamp' : { '$lte' : ?1}}",
            sort = "{'timestamp': 1 }")
    @NonNull
    List<AirDeviceData> findAllByDeviceIdAndTimestampBefore(String deviceId, Instant startTime);
}
