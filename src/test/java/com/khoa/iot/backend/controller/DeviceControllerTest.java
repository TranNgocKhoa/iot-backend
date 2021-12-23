package com.khoa.iot.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.khoa.iot.backend.entity.AirDeviceData;
import com.khoa.iot.backend.enums.DefaultErrorType;
import com.khoa.iot.backend.enums.TemperatureUnit;
import com.khoa.iot.backend.model.AirData;
import com.khoa.iot.backend.model.TemperatureData;
import com.khoa.iot.backend.repository.DeviceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeviceControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private DeviceRepository deviceRepository;

    @BeforeEach
    public void setup() {
        deviceRepository.deleteAll();
    }

    @AfterEach
    public void clean() {
        deviceRepository.deleteAll();
    }

    @Test
    public void test404Error() {
        ResponseEntity<JsonNode> addressResponseEntity =
                restTemplate.exchange("/404", HttpMethod.GET, HttpEntity.EMPTY, JsonNode.class);

        assertThat(addressResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        String response = addressResponseEntity.getBody().toPrettyString();
        assertThat(response).contains(DefaultErrorType.HTTP_404.getMessage());
    }

    @Test
    public void test415Error() {
        ResponseEntity<JsonNode> addressResponseEntity =
                restTemplate.exchange("/api/devices", HttpMethod.POST, HttpEntity.EMPTY, JsonNode.class);

        assertThat(addressResponseEntity.getStatusCode()).isEqualTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        String response = Objects.requireNonNull(addressResponseEntity.getBody()).toPrettyString();
        assertThat(response).contains(DefaultErrorType.HTTP_415.getMessage());
    }

    @Test
    public void testInsertAirDeviceData_success() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        AirDeviceData airDeviceData = new AirDeviceData();
        airDeviceData.setDeviceId("xyz123");
        airDeviceData.setLatitude(41.25f);
        airDeviceData.setLongitude(-120.9762f);
        AirData airData = new AirData();
        airData.setHumidity(123);
        TemperatureData temperatureData = new TemperatureData();
        temperatureData.setUnit(TemperatureUnit.CELSIUS);
        temperatureData.setValue("23.3");
        airData.setTemperature(temperatureData);
        airDeviceData.setData(airData);

        HttpEntity<AirDeviceData> httpEntity = new HttpEntity<>(airDeviceData, httpHeaders);


        ResponseEntity<JsonNode> addressResponseEntity =
                restTemplate.exchange("/api/devices", HttpMethod.POST, httpEntity, JsonNode.class);

        assertThat(addressResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String response = addressResponseEntity.getBody().toPrettyString();
        assertThat(response).contains("true");
    }

    @Test
    public void testInsertAirDeviceData_payloadNull() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, httpHeaders);


        ResponseEntity<JsonNode> addressResponseEntity =
                restTemplate.exchange("/api/devices", HttpMethod.POST, httpEntity, JsonNode.class);

        assertThat(addressResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        String response = addressResponseEntity.getBody().toPrettyString();
        assertThat(response).contains(DefaultErrorType.HTTP_400.getMessage());
    }

    @Test
    public void testInsertAirDeviceData_payloadEmpty() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> httpEntity = new HttpEntity<>("", httpHeaders);


        ResponseEntity<JsonNode> addressResponseEntity = restTemplate.exchange("/api/devices", HttpMethod.POST, httpEntity, JsonNode.class);

        assertThat(addressResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        String response = addressResponseEntity.getBody().toPrettyString();
        assertThat(response).contains(DefaultErrorType.HTTP_400.getMessage());
    }

    @Test
    public void testGetTimeSeriesData_success() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, httpHeaders);

        String deviceId = "11111";

        List<AirDeviceData> airDeviceData = getAirDeviceData(deviceId);

        getSavedDataList(airDeviceData);

        UriComponents uriComponents = UriComponentsBuilder.fromPath("/api/devices/" + deviceId)
                .queryParam("startTime", Instant.now().minusSeconds(200).toString())
                .queryParam("endTime", Instant.now().plusSeconds(200).toString())
                .build();

        ResponseEntity<JsonNode> addressResponseEntity = restTemplate.exchange(uriComponents.toString(), HttpMethod.GET, httpEntity, JsonNode.class);

        assertThat(addressResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        JsonNode response = addressResponseEntity.getBody();
        assert response != null;
        assertThat(response.get("data")).isNotNull();
        assertThat(response.get("data").isArray()).isTrue();
        assertThat(response.get("data").size()).isEqualTo(airDeviceData.size());
    }

    @Test
    public void testGetTimeSeriesData_onlyDeviceId_success() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, httpHeaders);

        String deviceId = "11111";

        List<AirDeviceData> airDeviceData = getAirDeviceData(deviceId);

        getSavedDataList(airDeviceData);

        UriComponents uriComponents = UriComponentsBuilder.fromPath("/api/devices/" + deviceId)
                .build();

        ResponseEntity<JsonNode> addressResponseEntity = restTemplate.exchange(uriComponents.toString(), HttpMethod.GET, httpEntity, JsonNode.class);

        assertThat(addressResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        JsonNode response = addressResponseEntity.getBody();
        assert response != null;
        assertThat(response.get("data")).isNotNull();
        assertThat(response.get("data").isArray()).isTrue();
        assertThat(response.get("data").size()).isEqualTo(airDeviceData.size());
    }

    @Test
    public void testGetTimeSeriesData_onlyDeviceIdAndEndTime_success() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, httpHeaders);

        String deviceId = "11111";

        List<AirDeviceData> airDeviceData = getAirDeviceData(deviceId);

        List<AirDeviceData> savedDataList = getSavedDataList(airDeviceData);

        UriComponents uriComponents = UriComponentsBuilder.fromPath("/api/devices/" + deviceId)
                .queryParam("endTime", savedDataList.get(1).getTimestamp())
                .build();

        ResponseEntity<JsonNode> addressResponseEntity = restTemplate.exchange(uriComponents.toString(), HttpMethod.GET, httpEntity, JsonNode.class);

        assertThat(addressResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        JsonNode response = addressResponseEntity.getBody();
        assert response != null;
        assertThat(response.get("data")).isNotNull();
        assertThat(response.get("data").isArray()).isTrue();
        assertThat(response.get("data").size()).isEqualTo(airDeviceData.size() - 1);
    }

    private List<AirDeviceData> getSavedDataList(List<AirDeviceData> airDeviceData) {
        List<AirDeviceData> afterSave = new ArrayList<>();
        for (AirDeviceData airDeviceDatum : airDeviceData) {
            afterSave.add(deviceRepository.save(airDeviceDatum));

            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        return afterSave;
    }

    @Test
    public void testGetTimeSeriesData_onlyDeviceIdAndStartTime_success() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, httpHeaders);

        String deviceId = "11111";

        List<AirDeviceData> airDeviceData = getAirDeviceData(deviceId);

        List<AirDeviceData> savedDataList = getSavedDataList(airDeviceData);

        UriComponents uriComponents = UriComponentsBuilder.fromPath("/api/devices/" + deviceId)
                .queryParam("startTime", savedDataList.get(1).getTimestamp())
                .build();

        ResponseEntity<JsonNode> addressResponseEntity = restTemplate.exchange(uriComponents.toString(), HttpMethod.GET, httpEntity, JsonNode.class);

        assertThat(addressResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        JsonNode response = addressResponseEntity.getBody();
        assert response != null;
        assertThat(response.get("data")).isNotNull();
        assertThat(response.get("data").isArray()).isTrue();
        assertThat(response.get("data").size()).isEqualTo(airDeviceData.size() - 1);
    }

    private List<AirDeviceData> getAirDeviceData(String deviceId) {
        List<AirDeviceData> airDeviceDataList = new ArrayList<>();

        AirDeviceData airDeviceData1 = new AirDeviceData();
        airDeviceData1.setDeviceId(deviceId);
        airDeviceData1.setLongitude(1);
        airDeviceData1.setLatitude(2);
        airDeviceDataList.add(airDeviceData1);

        AirDeviceData airDeviceData2 = new AirDeviceData();
        airDeviceData2.setDeviceId(deviceId);
        airDeviceData2.setLongitude(1);
        airDeviceData2.setLatitude(2);
        airDeviceDataList.add(airDeviceData2);

        AirDeviceData airDeviceData3 = new AirDeviceData();
        airDeviceData3.setDeviceId(deviceId);
        airDeviceData3.setLongitude(1);
        airDeviceData3.setLatitude(2);
        airDeviceDataList.add(airDeviceData3);

        return airDeviceDataList;
    }
}