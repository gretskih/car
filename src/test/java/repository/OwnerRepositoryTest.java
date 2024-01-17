package repository;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.car.model.Owner;
import ru.job4j.car.model.User;
import ru.job4j.car.repository.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static repository.ConfigurationTest.crudRepository;
import static repository.UserRepositoryTest.getUser;

class OwnerRepositoryTest {

    public static OwnerRepository ownerRepository = new OwnerRepositoryImpl(crudRepository);
    public static UserRepository userRepository = new UserRepositoryImpl(crudRepository);

    /**
     * Очистка базы
     */
    @BeforeAll
    static void clearTableBefore() {
        Session session = ConfigurationTest.sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM Owner").executeUpdate();
            session.createQuery("DELETE FROM User").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    /**
     * Фабрика владельцев
     */
    public static Owner getOwner(String name, User user) {
        return Owner.of()
                .name(name)
                .user(user)
                .build();
    }

    /**
     * Обновление записи
     */
    @Test
    public void whenUpdateOwnerThenGetUpdatedOwner() {
        User user = getUser("user1");
        userRepository.create(user);
        var expectedOwner = getOwner("owner1", user);
        ownerRepository.create(expectedOwner);

        expectedOwner.setName("owner2");
        boolean actualStatusTransaction = ownerRepository.update(expectedOwner);
        Optional<Owner> actualOwner = ownerRepository.findById(expectedOwner.getId());

        assertThat(actualStatusTransaction).isTrue();
        assertThat(actualOwner)
                .isPresent()
                .isNotEmpty()
                .contains(expectedOwner);
    }

    /**
     * Удаление записи
     */
    @Test
    public void whenDeleteOwnerThenGetEmpty() {
        User user = getUser("user3");
        userRepository.create(user);
        var expectedOwner = getOwner("owner3", user);
        ownerRepository.create(expectedOwner);

        boolean actualStatusTransaction = ownerRepository.delete(expectedOwner);
        Optional<Owner> actualOwner = ownerRepository.findById(expectedOwner.getId());

        assertThat(actualStatusTransaction).isTrue();
        assertThat(actualOwner).isEmpty();
    }

    /**
     * Получение полного списка записей
     */
    @Test
    public void whenFindAllOwnerOrderByIdThenGetAllOwners() {
        clearTableBefore();
        User user1 = getUser("user4");
        userRepository.create(user1);
        User user2 = getUser("user5");
        userRepository.create(user2);
        var expectedOwner1 = getOwner("owner4", user1);
        ownerRepository.create(expectedOwner1);

        var expectedOwner2 = getOwner("owner5", user2);
        ownerRepository.create(expectedOwner2);

        List<Owner> actualOwners = ownerRepository.findAll();
        assertThat(actualOwners).isEqualTo(List.of(expectedOwner1, expectedOwner2));
    }

    /**
     * Поиск записи по Id
     */
    @Test
    public void whenFindOwnerByIdThenGetOwner() {
        User user = getUser("user6");
        userRepository.create(user);
        var expectedOwner = getOwner("owner6", user);
        ownerRepository.create(expectedOwner);

        Optional<Owner> actualOwner = ownerRepository.findById(expectedOwner.getId());
        assertThat(actualOwner)
                .isPresent()
                .isNotEmpty()
                .contains(expectedOwner);
    }

    /**
     * Поиск по части имени
     */
    @Test
    public void whenFindOwnerByLikeNameThenGetOwner() {
        User user1 = getUser("user7");
        userRepository.create(user1);
        User user2 = getUser("user8");
        userRepository.create(user2);
        var expectedOwner1 = getOwner("owner7", user1);
        ownerRepository.create(expectedOwner1);

        var expectedOwner2 = getOwner("owner8", user2);
        ownerRepository.create(expectedOwner2);

        List<Owner> actualOwners = ownerRepository.findByLikeName("er8");
        assertThat(actualOwners).isEqualTo(List.of(expectedOwner2));
    }

    /**
     * Поиск по имени name
     */
    @Test
    public void whenFindOwnerByNameThenGetOwner() {
        User user = getUser("user9");
        userRepository.create(user);
        var expectedOwner = getOwner("owner9", user);
        ownerRepository.create(expectedOwner);

        Optional<Owner> actualOwner = ownerRepository.findByName(expectedOwner.getName());
        assertThat(actualOwner)
                .isPresent()
                .isNotEmpty()
                .contains(expectedOwner);
    }

    /**
     * Найти владельца по User
     */
    @Test
    public void whenFindOwnerByUserThenGetOwner() {
        User user = getUser("user10");
        userRepository.create(user);
        var expectedOwner = getOwner("owner10", user);
        ownerRepository.create(expectedOwner);

        Optional<Owner> actualOwner = ownerRepository.findByUser(user);
        assertThat(actualOwner)
                .isPresent()
                .isNotEmpty()
                .contains(expectedOwner);
    }

    /**
     * Очистка базы
     */
    @AfterAll
    static void clearTableAfter() {
        Session session = ConfigurationTest.sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM Owner").executeUpdate();
            session.createQuery("DELETE FROM User").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }
}