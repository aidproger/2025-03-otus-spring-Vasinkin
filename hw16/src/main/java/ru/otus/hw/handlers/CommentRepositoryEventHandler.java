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
import ru.otus.hw.models.Comment;
import ru.otus.hw.services.security.acl.AclServiceWrapperService;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@RepositoryEventHandler
@Component
public class CommentRepositoryEventHandler {

    private final AclServiceWrapperService aclServiceWrapperService;

    @PreAuthorize("canWrite(#comment.id, T(ru.otus.hw.models.Comment))")
    @HandleBeforeCreate
    public void validateCommentCreate(Comment comment) {
        log.debug("Validate create comment: {}", comment);
    }

    @Transactional
    @HandleAfterCreate
    public void createAclPermissions(Comment comment) {
        log.info("Create permission comment: {}", comment);
        aclServiceWrapperService.createPermissions(comment, Set.of(BasePermission.WRITE, BasePermission.DELETE));
    }

    @PreAuthorize("canDelete(#comment.id, T(ru.otus.hw.models.Comment))")
    @HandleBeforeDelete
    public void validateDeleteCommentDelete(Comment comment) {
        log.debug("Validate delete comment: {}", comment);
    }

    @Transactional
    @HandleAfterDelete
    public void deleteAclPermissions(Comment comment) {
        log.info("Delete permission comment: {}", comment);
        aclServiceWrapperService.deleteAllPermissions(comment);
    }

}
