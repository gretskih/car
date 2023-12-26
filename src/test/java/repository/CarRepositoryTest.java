package repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.car.model.*;
import ru.job4j.car.repository.CarRepository;
import ru.job4j.car.repository.CrudRepository;
import ru.job4j.car.repository.EngineRepository;
import ru.job4j.car.repository.OwnerRepository;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class CarRepositoryTest {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();
    private final CrudRepository crudRepository = new CrudRepository(sf);
    private final CarRepository carRepository = new CarRepository(crudRepository);
    private final EngineRepository engineRepository = new EngineRepository(crudRepository);
    private final OwnerRepository ownerRepository = new OwnerRepository(crudRepository);

    /**
     * Очистка базы
     */
    @BeforeEach
    public void clearTable() {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM Engine").executeUpdate();
            session.createQuery("DELETE FROM Owner").executeUpdate();
            session.createQuery("DELETE FROM Photo").executeUpdate();
            session.createQuery("DELETE FROM Car").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    private Car getCar(String name) {
        Engine engine = new Engine();
        engine.setName("engine");
        engineRepository.create(engine);

        PeriodHistory periodHistory = new PeriodHistory();
        crudRepository.run(session -> session.persist(periodHistory));

        Owner owner = new Owner();
        owner.setName("owner");
        owner.setHistory(periodHistory);
        ownerRepository.create(owner);

        PeriodHistory periodHistorySet = new PeriodHistory();
        crudRepository.run(session -> session.persist(periodHistorySet));

        Owner ownerSet = new Owner();
        ownerSet.setName("ownerSet");
        ownerSet.setHistory(periodHistorySet);
        ownerRepository.create(ownerSet);

        Photo photo = new Photo();
        photo.setName("one");
        photo.setPath("///");
        crudRepository.run(session -> session.persist(photo));

        Car expectedCar = new Car();
        expectedCar.setName(name);
        expectedCar.setBrand("BMW");
        expectedCar.setEngine(engine);
        expectedCar.setOwner(owner);
        expectedCar.setPhotos(Set.of(photo));
        expectedCar.setOwners(Set.of(ownerSet));

        return expectedCar;
    }

    /**
     * Обновление записи
     */
    @Test
    public void whenUpdateCarThenGetUpdatedCar() {
        /*
        Car expectedCar = getCar("test1");
        System.out.println(expectedCar);
        carRepository.create(expectedCar);
        expectedCar.setName("test2");

        carRepository.update(expectedCar);
        Optional<Car> actualCar = carRepository.findById(expectedCar.getId());

        assertThat(actualCar)
                .isPresent()
                .isNotEmpty()
                .contains(expectedCar);

         */
    }
}
