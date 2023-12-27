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

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class CarRepositoryTest {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();
    private final CarRepository carRepository = new CarRepository(new CrudRepository(sf));

    /**
     * Очистка базы
     */
    @BeforeEach
    public void clearTable() {
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
     * Получить объект автомобиль
     * @param name название
     * @return автомобиль с name
     */
    private Car getCar(String name) {
        Engine engine = new Engine();
        engine.setName("engine");

        PeriodHistory periodHistory = new PeriodHistory();
        Owner owner = new Owner();
        owner.setName("owner");
        owner.setHistory(periodHistory);

        PeriodHistory periodHistorySet = new PeriodHistory();
        Owner ownerSet = new Owner();
        ownerSet.setName("ownerSet");
        ownerSet.setHistory(periodHistorySet);

        Photo photo = new Photo();
        photo.setName("one");
        photo.setPath("///");

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
     * Создание/обновление записи
     */
    @Test
    public void whenUpdateCarThenGetUpdatedCar() {
        Car expectedCar = getCar("test1");
        carRepository.create(expectedCar);
        expectedCar.setName("test2");

        carRepository.update(expectedCar);

        Optional<Car> actualCar = carRepository.findById(expectedCar.getId());
        assertThat(actualCar)
                .isPresent()
                .isNotEmpty()
                .contains(expectedCar);
    }

    /**
     * Создание/удаление записи
     */
    @Test
    public void whenDeleteCarThenGetEmptyCar() {
        Car expectedCar = getCar("test2");
        carRepository.create(expectedCar);

        carRepository.delete(expectedCar.getId());

        Optional<Car> actualCar = carRepository.findById(expectedCar.getId());
        assertThat(actualCar).isEmpty();
    }
}
