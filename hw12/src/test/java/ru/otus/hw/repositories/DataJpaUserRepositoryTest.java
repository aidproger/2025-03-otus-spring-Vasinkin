package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.otus.hw.common.TestDataGenerator;
import ru.otus.hw.models.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе DATA JPA для работы с пользователями ")
@DataJpaTest
public class DataJpaUserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager em;

    private static final int EXPECTED_NUMBER_OF_USERS = 3;

    private List<User> dbUsers;

    @BeforeEach
    void setUp() {
        dbUsers = TestDataGenerator.generateExpectedDbUsers(em, EXPECTED_NUMBER_OF_USERS);
    }

    @DisplayName("должен загружать список всех пользователей ")
    @Test
    void shouldReturnCorrectUsersList() {
        var actualUsers = userRepository.findAll();
        var expectedUsers = dbUsers;

        assertThat(actualUsers).containsExactlyElementsOf(expectedUsers);
        actualUsers.forEach(System.out::println);

        var bb=new BCryptPasswordEncoder(10);
        expectedUsers.forEach(user->{
            String encodedPassword = bb.encode(user.getPassword());
            System.out.println(user.getLogin()+" pass='"+encodedPassword+"'");
        });

    }

    @DisplayName("должен загружать пользователя по логину ")
    @Test
    void shouldReturnCorrectUserByLogin() {
        var expectedUser = em.find(User.class, 1);
        var actualUser = userRepository.findByLogin(expectedUser.getLogin());
        assertThat(actualUser).isPresent()
                .get().isEqualTo(expectedUser);
        System.out.println(actualUser);
    }
}
