package ru.otus.hw.services.security.acl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.test.context.support.WithUserDetails;
import ru.otus.hw.common.TestDataGenerator;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("Сервис взаимодействия с привелегии доступа acl ")
@SpringBootTest
public class AclServiceWrapperServiceImplTest {

    @Autowired
    private AclServiceWrapperService aclServiceWrapperService;

    @DisplayName("должен удалять привелегии доступа acl у удаляемых экземпляров объектов ")
    @WithUserDetails(value = "login_1")
    @Test
    void shouldDeletePermissions() {
        assertThatCode(() -> aclServiceWrapperService.deleteAllPermissions(TestDataGenerator.generateComment())).doesNotThrowAnyException();
    }

    @DisplayName("должен добавлять привелегии доступа acl к новым экземплярам объектов ")
    @WithUserDetails(value = "login_1")
    @Test
    void shouldCreateNewPermissions() {
        assertThatCode(() -> aclServiceWrapperService.createPermissions(
                TestDataGenerator.generateComment(), Set.of(BasePermission.WRITE, BasePermission.DELETE)))
                .doesNotThrowAnyException();
    }

}
