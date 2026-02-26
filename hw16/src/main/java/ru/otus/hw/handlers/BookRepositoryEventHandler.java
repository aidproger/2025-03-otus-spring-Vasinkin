package ru.otus.hw.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.security.acl.AclServiceWrapperService;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@RepositoryEventHandler
@Component
public class BookRepositoryEventHandler {

    private final AclServiceWrapperService aclServiceWrapperService;

    @PreAuthorize("canWrite(#book.id, T(ru.otus.hw.models.Book))")
    @HandleBeforeCreate
    public void validateBookCreate(Book book) {
        log.debug("Validate create book: {}", book);
    }

    @Transactional
    @HandleAfterCreate
    public void createAclPermissions(Book book) {
        log.info("Create permission book: {}", book);
        aclServiceWrapperService.createPermissions(book, Set.of(BasePermission.WRITE, BasePermission.DELETE));
    }

    @PreAuthorize("canDelete(#book.id, T(ru.otus.hw.models.Book))")
    @HandleBeforeDelete
    public void validateDeleteBookDelete(Book book) {
        log.debug("Validate delete book: {}", book);
    }

    @Transactional
    @HandleAfterDelete
    public void deleteAclPermissions(Book book) {
        log.info("Delete permission book: {}", book);
        aclServiceWrapperService.deleteAllPermissions(book);
    }

}
