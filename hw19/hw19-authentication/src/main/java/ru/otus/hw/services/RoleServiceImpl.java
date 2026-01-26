package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.domain.RoleDto;
import ru.otus.hw.exceptions.RoleNotFoundException;
import ru.otus.hw.repositories.RoleRepository;

import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    @Override
    public List<RoleDto> findAll() {
        log.info("Find all data");
        var roles = roleRepository.findAll().stream()
                .map(r -> new RoleDto(r.getId(), r.getName()))
                .toList();
        if (isEmpty(roles)) {
            throw new RoleNotFoundException();
        }
        return roles;
    }
}
