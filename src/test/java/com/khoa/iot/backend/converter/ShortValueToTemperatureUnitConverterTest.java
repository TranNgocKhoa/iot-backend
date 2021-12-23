package com.khoa.iot.backend.converter;

import com.khoa.iot.backend.enums.TemperatureUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ShortValueToTemperatureUnitConverterTest {
    @InjectMocks
    ShortValueToTemperatureUnitConverter shortValueToTemperatureUnitConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidShortNotion_shouldReturnCorrectEnum() {
        assertEquals(shortValueToTemperatureUnitConverter.convert("C"), TemperatureUnit.CELSIUS);
    }

    @Test
    void givenInvalidShortNotion_shouldReturnNull() {
        assertNull(shortValueToTemperatureUnitConverter.convert("NotValid"));
    }

    @Test
    void givenShortNotionNull_shouldReturnNull() {
        assertNull(shortValueToTemperatureUnitConverter.convert(null));
    }
}