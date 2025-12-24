package ru.otus.hw.services.acl;

import org.springframework.security.acls.model.Permission;

import java.util.Set;

public interface AclServiceWrapperService {

    void createPermissions(Object object, Set<Permission> permissions);

    void deleteAllPermissions(Object object);
}
