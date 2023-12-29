package repository;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.car.model.Owner;
import ru.job4j.car.model.PeriodHistory;
import ru.job4j.car.repository.CrudRepository;
import ru.job4j.car.repository.OwnerRepositoryImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class OwnerRepositoryTest {

    private final OwnerRepositoryImpl ownerRepository = new OwnerRepositoryImpl(new CrudRepository(ConfigurationTest.sf));

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
        PeriodHistory periodHistory = new PeriodHistory();

        Owner expectedOwner = new Owner();
        expectedOwner.setName("owner1");
        expectedOwner.setHistory(periodHistory);
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
        PeriodHistory periodHistory = new PeriodHistory();

        Owner expectedOwner = new Owner();
        expectedOwner.setName("owner3");
        expectedOwner.setHistory(periodHistory);
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
        PeriodHistory periodHistory1 = new PeriodHistory();

        Owner expectedOwner1 = new Owner();
        expectedOwner1.setName("owner4");
        expectedOwner1.setHistory(periodHistory1);
        ownerRepository.create(expectedOwner1);

        PeriodHistory periodHistory2 = new PeriodHistory();

        Owner expectedOwner2 = new Owner();
        expectedOwner2.setName("owner5");
        expectedOwner2.setHistory(periodHistory2);
        ownerRepository.create(expectedOwner2);

        List<Owner> actualOwners = ownerRepository.findAllOrderById();
        assertThat(actualOwners).isEqualTo(List.of(expectedOwner1, expectedOwner2));
    }

    /**
     * Поиск записи по Id
     */
    @Test
    public void whenCreateNewOwnerThenGetOwnerById() {
        PeriodHistory periodHistory = new PeriodHistory();

        Owner expectedOwner = new Owner();
        expectedOwner.setName("owner6");
        expectedOwner.setHistory(periodHistory);
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
        PeriodHistory periodHistory1 = new PeriodHistory();

        Owner expectedOwner1 = new Owner();
        expectedOwner1.setName("owner7");
        expectedOwner1.setHistory(periodHistory1);
        ownerRepository.create(expectedOwner1);

        PeriodHistory periodHistory2 = new PeriodHistory();

        Owner expectedOwner2 = new Owner();
        expectedOwner2.setName("owner8");
        expectedOwner2.setHistory(periodHistory2);
        ownerRepository.create(expectedOwner2);

        List<Owner> actualOwners = ownerRepository.findByLikeName("er8");
        assertThat(actualOwners).isEqualTo(List.of(expectedOwner2));
    }

    /**
     * Поиск по имени name
     */
    @Test
    public void whenCreateNewOwnerThenGetByName() {
        PeriodHistory periodHistory = new PeriodHistory();

        Owner expectedOwner = new Owner();
        expectedOwner.setName("owner9");
        expectedOwner.setHistory(periodHistory);
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