package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.WellConverter;
import ru.otus.hw.domain.WellDto;
import ru.otus.hw.exceptions.WellNotFoundException;
import ru.otus.hw.repositories.WellRepository;

import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class WellServiceImpl implements WellService {

    private final Logger log = LoggerFactory.getLogger(WellServiceImpl.class);

    private final WellRepository wellRepository;

    private final WellConverter wellConverter;

    @Transactional(readOnly = true)
    @Override
    public List<WellDto> findAll() {
        log.info("Find all data");
        var wells = wellRepository.findAll().stream()
                .map(wellConverter::convertEntityToDto)
                .toList();
        if (isEmpty(wells)) {
            throw new WellNotFoundException();
        }
        return wells;
    }
}
