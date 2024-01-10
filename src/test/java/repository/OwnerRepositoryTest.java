package repository;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.car.model.Owner;
import ru.job4j.car.model.PeriodHistory;
import ru.job4j.car.repository.CrudRepository;
import ru.job4j.car.repository.OwnerRepository;
import ru.job4j.car.repository.OwnerRepositoryImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class OwnerRepositoryTest {

    private final OwnerRepository ownerRepository = new OwnerRepositoryImpl(new CrudRepository(ConfigurationTest.sf));

    /**
     * Очистка базы
     */
    @BeforeAll
    static void clearTableBefore() {
        Session session = ConfigurationTest.sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM Owner").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    /**
     * Обновление записи
     */
    @Test
    public void whenUpdateOwnerThenGetUpdatedOwner() {
        var periodHistory = PeriodHistory.of().build();
        var expectedOwner = Owner.of()
                .name("owner1")
                .ownerId(1)
                .build();
        ownerRepository.create(expectedOwner);

        expectedOwner.setName("owner2");
        ownerRepository.update(expectedOwner);
        Optional<Owner> actualOwner = ownerRepository.findById(expectedOwner.getId());

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
        var periodHistory = PeriodHistory.of().build();

        var expectedOwner = Owner.of()
                .name("owner3")
                .ownerId(3)
                .build();
        ownerRepository.create(expectedOwner);

        ownerRepository.delete(expectedOwner.getId());
        Optional<Owner> actualOwner = ownerRepository.findById(expectedOwner.getId());

        assertThat(actualOwner).isEmpty();
    }

    /**
     * Получение полного списка записей
     */
    @Test
    public void whenCreateNewOwnerThenGetAllOwners() {
        clearTableBefore();
        var periodHistory1 = PeriodHistory.of().build();

        var expectedOwner1 = Owner.of()
                .name("owner4")
                .ownerId(4)
                .build();
        ownerRepository.create(expectedOwner1);

        var periodHistory2 = PeriodHistory.of().build();

        var expectedOwner2 = Owner.of()
                .name("owner5")
                .ownerId(5)
                .build();
        ownerRepository.create(expectedOwner2);

        List<Owner> actualOwners = ownerRepository.findAllOrderById();
        assertThat(actualOwners).isEqualTo(List.of(expectedOwner1, expectedOwner2));
    }

    /**
     * Поиск записи по Id
     */
    @Test
    public void whenCreateNewOwnerThenGetOwnerById() {
        var periodHistory = PeriodHistory.of().build();

        var expectedOwner = Owner.of()
                .name("owner6")
                .ownerId(6)
                .build();
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
    public void whenCreateNewOwnerThenGetByLikeName() {
        var periodHistory1 = PeriodHistory.of().build();
        var expectedOwner1 = Owner.of()
                .name("owner7")
                .ownerId(7)
                .build();
        ownerRepository.create(expectedOwner1);

        var periodHistory2 = PeriodHistory.of().build();
        var expectedOwner2 = Owner.of()
                .name("owner8")
                .ownerId(8)
                .build();
        ownerRepository.create(expectedOwner2);

        List<Owner> actualOwners = ownerRepository.findByLikeName("er8");
        assertThat(actualOwners).isEqualTo(List.of(expectedOwner2));
    }

    /**
     * Поиск по имени name
     */
    @Test
    public void whenCreateNewOwnerThenGetByName() {
        var periodHistory = PeriodHistory.of().build();
        var expectedOwner = Owner.of()
                .name("owner9")
                .ownerId(9)
                .build();
        ownerRepository.create(expectedOwner);

        Optional<Owner> actualOwner = ownerRepository.findByName(expectedOwner.getName());
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
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }
}