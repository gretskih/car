package repository;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.car.model.User;
import ru.job4j.car.repository.UserRepository;
import ru.job4j.car.repository.UserRepositoryImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static repository.ConfigurationTest.crudRepository;

public class UserRepositoryTest {

    public static UserRepository userRepository = new UserRepositoryImpl(crudRepository);

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
     * Фабрика пользователей
     * @param login логин
     * @return User
     */
    public static User getUser(String login) {
        User user = new User();
        user.setName("Тест");
        user.setLogin(login);
        user.setPassword("0000");
        user.setContact("login@login.com");
        return user;
    }

    /**
     * Обновление пользователя
     */
    @Test
    public void whenUpdateUserThenGetUpdatedUser() {
        User expectedUser = getUser("user1");
        userRepository.create(expectedUser);
        expectedUser.setLogin("user2");

        boolean actualStatusTransaction = userRepository.update(expectedUser);
        Optional<User> actualUser = userRepository.findById(expectedUser.getId());

        assertThat(actualStatusTransaction).isTrue();
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
        User expectedUser = getUser("user3");
        userRepository.create(expectedUser);

        boolean actualStatusTransaction = userRepository.delete(expectedUser.getId());
        Optional<User> actualUser = userRepository.findById(expectedUser.getId());

        assertThat(actualStatusTransaction).isTrue();
        assertThat(actualUser).isEmpty();
    }

    /**
     * Получение всего списка пользователей
     */
    @Test
    public void whenFindAllUsersOrderByIdThenGetAllUsers() {
        clearTableBefore();
        User expectedUser1 = getUser("user4");
        userRepository.create(expectedUser1);

        User expectedUser2 = getUser("user5");
        userRepository.create(expectedUser2);

        List<User> actualUsers = userRepository.findAll();
        assertThat(actualUsers).isEqualTo(List.of(expectedUser1, expectedUser2));
    }

    /**
     * Поиск по Id
     */
    @Test
    public void whenFindUserByIdThenGetUser() {
        User expectedUser = getUser("user6");
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
    public void whenFindUserByLikeLoginThenGetUser() {
        User expectedUser1 = getUser("user7");
        userRepository.create(expectedUser1);

        User expectedUser2 = getUser("user8");
        userRepository.create(expectedUser2);

        List<User> actualUsers = userRepository.findByLikeLogin("er8");
        assertThat(actualUsers).isEqualTo(List.of(expectedUser2));
    }

    /**
     * Поиск по логину login
     */
    @Test
    public void whenFindUserByLoginThenGetUser() {
        User expectedUser = getUser("user9");
        userRepository.create(expectedUser);

        Optional<User> actualUser = userRepository.findByLogin(expectedUser.getLogin());
        assertThat(actualUser)
                .isPresent()
                .isNotEmpty()
                .contains(expectedUser);
    }

    /**
     * Поиск по login и password
     */
    @Test
    public void whenFindUserByLoginAndPasswordThenGetUser() {
        User expectedUser = getUser("user10");
        expectedUser.setPassword("5dfds1");
        userRepository.create(expectedUser);

        Optional<User> actualUser = userRepository
                .findByLoginAndPassword(expectedUser.getLogin(), expectedUser.getPassword());
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
