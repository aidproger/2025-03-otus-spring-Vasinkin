package ru.otus.hw.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocaleLocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {


    @Autowired
    private MessageSource messageSource;

    @Override
    public void serialize(LocalDateTime value, JsonGenerator jsonGenerator,
                          SerializerProvider serializers) throws IOException {
        var locale = LocaleContextHolder.getLocale();
        var dateTimeFormatterPattern = messageSource.getMessage("date-time-formatter-pattern", null,
                locale);
        var dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormatterPattern, locale);

        jsonGenerator.writeString(value.format(dateTimeFormatter));
    }
}
