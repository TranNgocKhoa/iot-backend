package com.khoa.iot.backend.config;

import com.khoa.iot.backend.converter.ShortValueToTemperatureUnitConverter;
import com.khoa.iot.backend.converter.TemperatureUnitToShortValueConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableMongoAuditing
@RequiredArgsConstructor
public class MongoConfiguration {
    private final ShortValueToTemperatureUnitConverter shortValueToTemperatureUnitConverter;
    private final TemperatureUnitToShortValueConverter temperatureUnitToShortValueConverter;

    @Bean
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(shortValueToTemperatureUnitConverter);
        converters.add(temperatureUnitToShortValueConverter);

        return new MongoCustomConversions(converters);
    }
}
