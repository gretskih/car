package repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.car.model.User;
import ru.job4j.car.repository.CrudRepository;
import ru.job4j.car.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class UserRepositoryTest {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();
    private final UserRepository userRepository = new UserRepository(new CrudRepository(sf));

    /**
     * Очистка базы
     */
    @BeforeEach
    public void clearTableUser() {
        Session session = sf.openSession();
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
        expectedUser.setLogin("user");
        expectedUser.setPassword("0000");
        userRepository.create(expectedUser);

        expectedUser.setLogin("user1");
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
    public void whenDeleteUserThenGetEmptyList() {
        User expectedUser = new User();
        expectedUser.setLogin("user");
        expectedUser.setPassword("0000");
        userRepository.create(expectedUser);

        userRepository.delete(expectedUser.getId());
        List<User> actualUsers = userRepository.findAllOrderById();

        assertThat(actualUsers).isEmpty();
    }

    /**
     * Получение всего списка пользователей
     */
    @Test
    public void whenCreateNewUserThenGetAllUsers() {
        User expectedUser1 = new User();
        expectedUser1.setLogin("user1");
        expectedUser1.setPassword("0000");
        userRepository.create(expectedUser1);
        User expectedUser2 = new User();
        expectedUser2.setLogin("user2");
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
        expectedUser.setLogin("user");
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
        expectedUser1.setLogin("user");
        expectedUser1.setPassword("0000");
        userRepository.create(expectedUser1);
        User expectedUser2 = new User();
        expectedUser2.setLogin("test");
        expectedUser2.setPassword("0000");
        userRepository.create(expectedUser2);

        List<User> actualUsers = userRepository.findByLikeLogin("es");
        assertThat(actualUsers).isEqualTo(List.of(expectedUser2));
    }

    /**
     * Поиск по логину login
     */
    @Test
    public void whenCreateNewUserThenGetByLogin() {
        User expectedUser = new User();
        expectedUser.setLogin("user");
        expectedUser.setPassword("0000");
        userRepository.create(expectedUser);

        Optional<User> actualUser = userRepository.findByLogin(expectedUser.getLogin());
        assertThat(actualUser)
                .isPresent()
                .isNotEmpty()
                .contains(expectedUser);
    }
}
