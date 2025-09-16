package ru.otus.hw.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.hw.domain.GenreDto;

@Component
public class StringToGenreDtoConverter implements Converter<String, GenreDto> {

    @Override
    public GenreDto convert(String source) {
        try {
            long id = Long.parseLong(source);
            return new GenreDto(id, "");
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
