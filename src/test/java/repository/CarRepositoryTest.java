package repository;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.car.model.*;
import ru.job4j.car.repository.car.CarRepository;
import ru.job4j.car.repository.car.HibernateCarRepository;
import ru.job4j.car.repository.engine.EngineRepository;
import ru.job4j.car.repository.engine.HibernateEngineRepository;
import ru.job4j.car.repository.owner.OwnerRepository;
import ru.job4j.car.repository.owner.HibernateOwnerRepository;
import ru.job4j.car.repository.user.UserRepository;
import ru.job4j.car.repository.user.HibernateUserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static repository.ConfigurationTest.crudRepository;
import static repository.OwnerRepositoryTest.getOwner;
import static repository.UserRepositoryTest.getUser;

public class CarRepositoryTest {

    public static CarRepository carRepository = new HibernateCarRepository(crudRepository);
    public static EngineRepository engineRepository = new HibernateEngineRepository(crudRepository);
    public static OwnerRepository ownerRepository = new HibernateOwnerRepository(crudRepository);
    public static UserRepository userRepository = new HibernateUserRepository(crudRepository);
    public static int yearCar = 1950;

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
            session.createQuery("DELETE FROM Body").executeUpdate();
            session.createQuery("DELETE FROM Brand").executeUpdate();
            session.createQuery("DELETE FROM Color").executeUpdate();
            session.createQuery("DELETE FROM Fuel").executeUpdate();
            session.createQuery("DELETE FROM Gearbox").executeUpdate();
            session.createQuery("DELETE FROM Photo").executeUpdate();
            session.createQuery("DELETE FROM Year").executeUpdate();
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
    static Car getCar(String name) throws Exception {
        Car expectedCar = new Car();
        Engine engine = new Engine();
        engine.setName("engine" + name);
        engineRepository.create(engine);
        expectedCar.setEngine(engine);
        expectedCar.setName(name);
        Brand expectedBrand = new Brand();
        expectedBrand.setName("brand" + name);
        crudRepository.run(session -> session.persist(expectedBrand));
        expectedCar.setBrand(expectedBrand);
        expectedCar.setMileage(1000);
        Year expectedYear = new Year();
        expectedYear.setYear(yearCar++);
        crudRepository.run(session -> session.persist(expectedYear));
        expectedCar.setYear(expectedYear);
        Body expectedBody = new Body();
        expectedBody.setBodyType("body" + name);
        crudRepository.run(session -> session.persist(expectedBody));
        expectedCar.setBody(expectedBody);
        Gearbox expectedGearbox = new Gearbox();
        expectedGearbox.setType("gearbox" + name);
        crudRepository.run(session -> session.persist(expectedGearbox));
        expectedCar.setGearbox(expectedGearbox);
        Fuel expectedFuel = new Fuel();
        expectedFuel.setType("fuel" + name);
        crudRepository.run(session -> session.persist(expectedFuel));
        expectedCar.setFuel(expectedFuel);
        Color expectedColor = new Color();
        expectedColor.setName("color" + name);
        crudRepository.run(session -> session.persist(expectedColor));
        expectedCar.setColor(expectedColor);
        expectedCar.setType("С пробегом");
        var user = getUser("user" + name);
        userRepository.create(user);
        var owner = getOwner("owner" + name, user);
        ownerRepository.create(owner);
        expectedCar.setOwner(owner);
        expectedCar.setHistoryOwners(Set.of(owner));
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
    public void whenUpdateCarThenGetUpdatedCar() throws Exception {
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
    public void whenDeleteCarThenGetEmptyCar() throws Exception {
        Car expectedCar = getCar("test3");
        carRepository.create(expectedCar);

        boolean actualStatusTransaction = carRepository.delete(expectedCar);
        Optional<Car> actualCar = carRepository.findById(expectedCar.getId());

        assertThat(actualStatusTransaction).isTrue();
        assertThat(actualCar).isEmpty();
    }

    /**
     * Получение полного списка записей
     */
    @Test
    public void whenFindAllCarsOrderByIdThenGetAllCars() throws Exception {
        clearTablesBefore();
        Car expectedCar1 = getCar("test4");
        carRepository.create(expectedCar1);
        Car expectedCar2 = getCar("test5");
        carRepository.create(expectedCar2);

        List<Car> actualCars = carRepository.findAll();

        assertThat(actualCars).isEqualTo(List.of(expectedCar1, expectedCar2));
    }

    /**
     * Поиск записи по Id
     */
    @Test
    public void whenFindCarByIdThenGetCar() throws Exception {
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
    public void whenFindByLikeNameThenGetCar() throws Exception {
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
    public void whenFindCarByNameThenGetCar() throws Exception {
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
    public void whenFindCarByUserThenGetCar() throws Exception {
        Car expectedCar = getCar("test10");
        carRepository.create(expectedCar);

        List<Car> actualCars = carRepository.findByUserId(expectedCar.getOwner().getUser().getId());
        assertThat(actualCars).isEqualTo(List.of(expectedCar));
    }

    /**
     * Получение полного списка записей (Lazy)
     */
    @Test
    public void whenFindAllCarsThenGetAllCars() throws Exception {
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
            session.createQuery("DELETE FROM Body").executeUpdate();
            session.createQuery("DELETE FROM Brand").executeUpdate();
            session.createQuery("DELETE FROM Color").executeUpdate();
            session.createQuery("DELETE FROM Fuel").executeUpdate();
            session.createQuery("DELETE FROM Gearbox").executeUpdate();
            session.createQuery("DELETE FROM Photo").executeUpdate();
            session.createQuery("DELETE FROM Year").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }
}
