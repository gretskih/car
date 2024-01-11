package repository;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.car.model.*;
import ru.job4j.car.repository.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static repository.ConfigurationTest.crudRepository;
import static repository.OwnerRepositoryTest.getOwner;
import static repository.UserRepositoryTest.getUser;

public class CarRepositoryTest {

    public static CarRepository carRepository = new CarRepositoryImpl(crudRepository);
    public static EngineRepository engineRepository = new EngineRepositoryImpl(crudRepository);
    public static OwnerRepository ownerRepository = new OwnerRepositoryImpl(crudRepository);
    public static UserRepository userRepository = new UserRepositoryImpl(crudRepository);
    public static BrandRepository brandRepository = new BrandRepositoryImpl(crudRepository);
    public static ColorRepository colorRepository = new ColorRepositoryImpl(crudRepository);
    public static YearRepository yearRepository = new YearRepositoryImpl(crudRepository);
    public static BodyRepository bodyRepository = new BodyRepositoryImpl(crudRepository);
    public static GearboxRepository gearboxRepository = new GearboxRepositoryImpl(crudRepository);
    public static FuelRepository fuelRepository = new FuelRepositoryImpl(crudRepository);

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
     * Получить объект автомобиль
     * @param name название
     * @return автомобиль с name
     */
    static Car getCar(String name) {
        Engine engine = new Engine();
        engine.setName("engine" + name);
        engineRepository.create(engine);

        Car expectedCar = new Car();
        expectedCar.setName(name);
        expectedCar.setBrand(brandRepository.findById(4).get());
        expectedCar.setMileage(10000);
        expectedCar.setYear(yearRepository.findById(66).get());
        expectedCar.setBody(bodyRepository.findById(1).get());
        expectedCar.setGearbox(gearboxRepository.findById(1).get());
        expectedCar.setFuel(fuelRepository.findById(1).get());
        expectedCar.setColor(colorRepository.findById(31).get());
        expectedCar.setType("б/у");
        expectedCar.setEngine(engine);

        var user = getUser("user" + name);
        userRepository.create(user);

        var owner = getOwner("owner" + name, user);
        ownerRepository.create(owner);

        expectedCar.setOwner(owner);
        expectedCar.setOwners(Set.of(owner));

        expectedCar.setPeriodHistories(Set.of(PeriodHistory.of().build()));

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

        boolean actualStatusTransaction = carRepository.update(expectedCar);
        Optional<Car> actualCar = carRepository.findById(expectedCar.getId());

        assertThat(actualStatusTransaction).isTrue();
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

        boolean actualStatusTransaction = carRepository.delete(expectedCar.getId());
        Optional<Car> actualCar = carRepository.findById(expectedCar.getId());

        assertThat(actualStatusTransaction).isTrue();
        assertThat(actualCar).isEmpty();
    }

    /**
     * Получение полного списка записей
     */
    @Test
    public void whenFindAllCarsOrderByIdThenGetAllCars() {
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
    public void whenFindCarByIdThenGetCar() {
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
    public void whenFindByLikeNameThenGetCar() {
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
    public void whenFindCarByNameThenGetCar() {
        Car expectedCar = getCar("test9");
        carRepository.create(expectedCar);

        Optional<Car> actualCar = carRepository.findByName(expectedCar.getName());
        assertThat(actualCar)
                .isPresent()
                .isNotEmpty()
                .contains(expectedCar);
    }

    /**
     * Поиск автомобилей по владельцу
     */
    @Test
    public void whenFindCarByUserThenGetCar() {
        Car expectedCar = getCar("test10");
        carRepository.create(expectedCar);

        List<Car> actualCars = carRepository.findByUser(expectedCar.getOwner().getUser());
        assertThat(actualCars).isEqualTo(List.of(expectedCar));
    }

    /**
     * Получение полного списка записей (Lazy)
     */
    @Test
    public void whenFindAllCarsThenGetAllCars() {
        clearTablesBefore();
        Car expectedCar1 = getCar("test11");
        carRepository.create(expectedCar1);
        Car expectedCar2 = getCar("test12");
        carRepository.create(expectedCar2);

        List<Car> actualCars = carRepository.findAll();

        assertThat(actualCars).isEqualTo(List.of(expectedCar1, expectedCar2));
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
