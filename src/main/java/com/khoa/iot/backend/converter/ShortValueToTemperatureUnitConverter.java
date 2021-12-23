package com.khoa.iot.backend.converter;

import com.khoa.iot.backend.enums.TemperatureUnit;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@ReadingConverter
public class ShortValueToTemperatureUnitConverter implements Converter<String, TemperatureUnit> {
    @Override
    public TemperatureUnit convert(@Nullable String source) {
        return Arrays.stream(TemperatureUnit.values())
                .filter(temperatureUnit -> temperatureUnit.getShortNotion().equals(source))
                .findFirst()
                .orElse(null);
    }
}
