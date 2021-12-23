package com.khoa.iot.backend.converter;

import com.khoa.iot.backend.enums.TemperatureUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TemperatureUnitToShortValueConverterTest {
    @InjectMocks
    TemperatureUnitToShortValueConverter temperatureUnitToShortValueConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenTemperature_shouldReturnShortNotion() {
        Arrays.stream(TemperatureUnit.values())
                .forEach(temperatureUnit -> assertEquals(temperatureUnitToShortValueConverter.convert(temperatureUnit), temperatureUnit.getShortNotion()));
    }

    @Test
    void givenNull_shouldReturnNull() {
        assertNull(temperatureUnitToShortValueConverter.convert(null));
    }
}