package repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.car.model.Owner;
import ru.job4j.car.model.PeriodHistory;
import ru.job4j.car.repository.CrudRepository;
import ru.job4j.car.repository.OwnerRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class OwnerRepositoryTest {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();
    private final CrudRepository crudRepository = new CrudRepository(sf);
    private final OwnerRepository ownerRepository = new OwnerRepository(crudRepository);

    /**
     * Очистка базы
     */
    @BeforeEach
    public void clearTableOwnerPeriodHistory() {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM Car").executeUpdate();
            session.createQuery("DELETE FROM Owner").executeUpdate();
            session.createQuery("DELETE FROM Engine").executeUpdate();
            session.createQuery("DELETE FROM Photo").executeUpdate();
            session.createQuery("DELETE FROM PeriodHistory").executeUpdate();
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
        expectedOwner.setName("owner");
        expectedOwner.setHistory(periodHistory);
        ownerRepository.create(expectedOwner);

        expectedOwner.setName("owner1");
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
    public void whenDeleteOwnerThenGetEmptyList() {
        PeriodHistory periodHistory = new PeriodHistory();

        Owner expectedOwner = new Owner();
        expectedOwner.setName("owner");
        expectedOwner.setHistory(periodHistory);
        ownerRepository.create(expectedOwner);

        ownerRepository.delete(expectedOwner.getId());
        List<Owner> actualOwners = ownerRepository.findAllOrderById();

        assertThat(actualOwners).isEmpty();
    }

    /**
     * Получение полного списка записей
     */
    @Test
    public void whenCreateNewOwnerThenGetAllOwners() {
        PeriodHistory periodHistory1 = new PeriodHistory();

        Owner expectedOwner1 = new Owner();
        expectedOwner1.setName("owner1");
        expectedOwner1.setHistory(periodHistory1);
        ownerRepository.create(expectedOwner1);

        PeriodHistory periodHistory2 = new PeriodHistory();

        Owner expectedOwner2 = new Owner();
        expectedOwner2.setName("owner2");
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
        expectedOwner.setName("owner");
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
        expectedOwner1.setName("owner1");
        expectedOwner1.setHistory(periodHistory1);
        ownerRepository.create(expectedOwner1);

        PeriodHistory periodHistory2 = new PeriodHistory();

        Owner expectedOwner2 = new Owner();
        expectedOwner2.setName("test");
        expectedOwner2.setHistory(periodHistory2);
        ownerRepository.create(expectedOwner2);

        List<Owner> actualOwners = ownerRepository.findByLikeName("es");
        assertThat(actualOwners).isEqualTo(List.of(expectedOwner2));
    }

    /**
     * Поиск по имени name
     */
    @Test
    public void whenCreateNewOwnerThenGetByName() {
        PeriodHistory periodHistory = new PeriodHistory();

        Owner expectedOwner = new Owner();
        expectedOwner.setName("owner");
        expectedOwner.setHistory(periodHistory);
        ownerRepository.create(expectedOwner);

        Optional<Owner> actualOwner = ownerRepository.findByName(expectedOwner.getName());
        assertThat(actualOwner)
                .isPresent()
                .isNotEmpty()
                .contains(expectedOwner);
    }
}