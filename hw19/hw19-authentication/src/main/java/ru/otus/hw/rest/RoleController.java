package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.domain.RoleDto;
import ru.otus.hw.services.RoleService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class RoleController {

    private final Logger log = LoggerFactory.getLogger(RoleController.class);

    private final RoleService roleService;

    @GetMapping("/api/v1/roles")
    public List<RoleDto> getAllUsers() {
        log.info("Request all data");
        return roleService.findAll();
    }

}
