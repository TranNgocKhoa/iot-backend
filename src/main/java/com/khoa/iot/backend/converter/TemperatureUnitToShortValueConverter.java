package com.khoa.iot.backend.converter;

import com.khoa.iot.backend.enums.TemperatureUnit;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@WritingConverter
public class TemperatureUnitToShortValueConverter implements Converter<TemperatureUnit, String> {
    @Override
    public String convert(@Nullable TemperatureUnit source) {
        return Optional.ofNullable(source)
                .map(TemperatureUnit::getShortNotion)
                .orElse(null);
    }
}
