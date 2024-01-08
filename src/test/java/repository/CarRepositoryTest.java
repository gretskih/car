package repository;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.car.model.*;
import ru.job4j.car.repository.CarRepository;
import ru.job4j.car.repository.CarRepositoryImpl;
import ru.job4j.car.repository.EngineRepository;
import ru.job4j.car.repository.EngineRepositoryImpl;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class CarRepositoryTest {

    private final CarRepository carRepository = new CarRepositoryImpl(ConfigurationTest.crudRepository);
    private static EngineRepository engineRepository = new EngineRepositoryImpl(ConfigurationTest.crudRepository);

    /**
     * Очистка базы
     */
    @BeforeAll
    static void clearTablesBefore() {
        Session session = ConfigurationTest.sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM Car").executeUpdate();
            session.createQuery("DELETE FROM Engine").executeUpdate();
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
    static Car getCar(String name) {
        Engine engine = new Engine();
        engine.setName("engine" + name);
        engineRepository.create(engine);

        var periodHistory = PeriodHistory.of().build();
        var owner = Owner.of()
                .name("owner1")
                .history(periodHistory)
                .ownerId(1)
                .build();

        Car expectedCar = new Car();
        expectedCar.setName(name);
        expectedCar.setBrand("BMW");
        expectedCar.setMileage(10000);
        expectedCar.setYearProduction(2020);
        expectedCar.setBodyType("Седан");
        expectedCar.setGearbox("Вариатор");
        expectedCar.setFuelType("Гибрид");
        expectedCar.setColour("Белый");
        expectedCar.setType("б/у");
        expectedCar.setEngine(engine);
        expectedCar.setOwner(owner);

        Photo photo = new Photo();
        photo.setName("photo" + name);
        photo.setPath("photoPath" + name);

        expectedCar.setPhotos(Set.of(photo));
        return expectedCar;
    }

    /**
     * Создание/обновление записи
     */
    @Test
    public void whenUpdateCarThenGetUpdatedCar() {
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
    }

    /**
     * Создание/удаление записи
     */
    @Test
    public void whenDeleteCarThenGetEmptyCar() {
        Car expectedCar = getCar("test3");
        carRepository.create(expectedCar);

        carRepository.delete(expectedCar.getId());

        Optional<Car> actualCar = carRepository.findById(expectedCar.getId());
        assertThat(actualCar).isEmpty();
    }

    /**
     * Получение полного списка записей
     */
    @Test
    public void whenCreateNewCarsThenGetAllCars() {
        clearTablesBefore();
        Car expectedCar1 = getCar("test4");
        carRepository.create(expectedCar1);
        Car expectedCar2 = getCar("test5");
        carRepository.create(expectedCar2);

        List<Car> actualCars = carRepository.findAllOrderById();

        assertThat(actualCars).isEqualTo(List.of(expectedCar1, expectedCar2));
    }

    /**
     * Поиск записи по Id
     */
    @Test
    public void whenCreateNewCarThenGetCarById() {
        Car expectedCar = getCar("test6");
        carRepository.create(expectedCar);

        Optional<Car> actualCar = carRepository.findById(expectedCar.getId());

        assertThat(actualCar)
                .isPresent()
                .isNotEmpty()
                .contains(expectedCar);
    }

    /**
     * Поиск по части названия name
     */
    @Test
    public void whenCreateNewCarThenGetByLikeName() {
        clearTablesBefore();
        Car expectedCar1 = getCar("car7");
        carRepository.create(expectedCar1);
        Car expectedCar2 = getCar("test8");
        carRepository.create(expectedCar2);

        List<Car> actualCars = carRepository.findByLikeName("st8");

        assertThat(actualCars).isEqualTo(List.of(expectedCar2));
    }

    /**
     * Поиск по названию name
     */
    @Test
    public void whenCreateNewCarThenGetByName() {
        Car expectedCar = getCar("test9");
        carRepository.create(expectedCar);

        Optional<Car> actualCar = carRepository.findByName(expectedCar.getName());
        assertThat(actualCar)
                .isPresent()
                .isNotEmpty()
                .contains(expectedCar);
    }

    /**
     * Очистка базы
     */
    @AfterAll
    static void clearTablesAfter() {
        Session session = ConfigurationTest.sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM Car").executeUpdate();
            session.createQuery("DELETE FROM Engine").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }
}
