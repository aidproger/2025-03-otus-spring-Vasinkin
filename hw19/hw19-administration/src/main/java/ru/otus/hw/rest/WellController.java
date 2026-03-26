package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.domain.WellDto;
import ru.otus.hw.services.WellService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WellController {

    private final Logger log = LoggerFactory.getLogger(WellController.class);

    private final WellService wellService;

    @GetMapping("/api/v1/wells")
    public List<WellDto> getAllWells() {
        log.info("Request all data");
        return wellService.findAll();
    }

}
