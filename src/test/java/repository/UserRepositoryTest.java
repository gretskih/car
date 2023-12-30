package repository;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.car.model.User;
import ru.job4j.car.repository.CrudRepository;
import ru.job4j.car.repository.UserRepository;
import ru.job4j.car.repository.UserRepositoryImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class UserRepositoryTest {

    private final UserRepository userRepository = new UserRepositoryImpl(new CrudRepository(ConfigurationTest.sf));

    /**
     * Очистка базы
     */
    @BeforeAll
    static void clearTableBefore() {
        Session session = ConfigurationTest.sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM User").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    /**
     * Обновление пользователя
     */
    @Test
    public void whenUpdateUserThenGetUpdatedUser() {
        User expectedUser = new User();
        expectedUser.setLogin("user1");
        expectedUser.setPassword("0000");
        userRepository.create(expectedUser);

        expectedUser.setLogin("user2");
        userRepository.update(expectedUser);
        Optional<User> actualUser = userRepository.findById(expectedUser.getId());

        assertThat(actualUser)
                .isPresent()
                .isNotEmpty()
                .contains(expectedUser);
    }

    /**
     * Удаление пользователя
     */
    @Test
    public void whenDeleteUserThenGetEmpty() {
        User expectedUser = new User();
        expectedUser.setLogin("user3");
        expectedUser.setPassword("0000");
        userRepository.create(expectedUser);

        userRepository.delete(expectedUser.getId());
        Optional<User> actualUser = userRepository.findById(expectedUser.getId());

        assertThat(actualUser).isEmpty();
    }

    /**
     * Получение всего списка пользователей
     */
    @Test
    public void whenCreateNewUserThenGetAllUsers() {
        clearTableBefore();
        User expectedUser1 = new User();
        expectedUser1.setLogin("user4");
        expectedUser1.setPassword("0000");
        userRepository.create(expectedUser1);
        User expectedUser2 = new User();
        expectedUser2.setLogin("user5");
        expectedUser2.setPassword("0000");
        userRepository.create(expectedUser2);

        List<User> actualUsers = userRepository.findAllOrderById();
        assertThat(actualUsers).isEqualTo(List.of(expectedUser1, expectedUser2));
    }

    /**
     * Поиск по Id
     */
    @Test
    public void whenCreateNewUserThenGetUserById() {
        User expectedUser = new User();
        expectedUser.setLogin("user6");
        expectedUser.setPassword("0000");
        userRepository.create(expectedUser);

        Optional<User> actualUser = userRepository.findById(expectedUser.getId());
        assertThat(actualUser)
                .isPresent()
                .isNotEmpty()
                .contains(expectedUser);
    }

    /**
     * Поиск по части логина
     */
    @Test
    public void whenCreateNewUserThenGetByLikeLogin() {
        User expectedUser1 = new User();
        expectedUser1.setLogin("user7");
        expectedUser1.setPassword("0000");
        userRepository.create(expectedUser1);
        User expectedUser2 = new User();
        expectedUser2.setLogin("user8");
        expectedUser2.setPassword("0000");
        userRepository.create(expectedUser2);

        List<User> actualUsers = userRepository.findByLikeLogin("er8");
        assertThat(actualUsers).isEqualTo(List.of(expectedUser2));
    }

    /**
     * Поиск по логину login
     */
    @Test
    public void whenCreateNewUserThenGetByLogin() {
        User expectedUser = new User();
        expectedUser.setLogin("user9");
        expectedUser.setPassword("0000");
        userRepository.create(expectedUser);

        Optional<User> actualUser = userRepository.findByLogin(expectedUser.getLogin());
        assertThat(actualUser)
                .isPresent()
                .isNotEmpty()
                .contains(expectedUser);
    }

    /**
     * Очистка базы
     */
    @AfterAll
    static void clearTableAfter() {
        Session session = ConfigurationTest.sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM User").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }
}
