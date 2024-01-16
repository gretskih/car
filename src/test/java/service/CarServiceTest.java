package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.job4j.car.model.*;
import ru.job4j.car.repository.CarRepository;
import ru.job4j.car.service.CarServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {
    @Mock
    private CarRepository carRepository;
    @InjectMocks
    private CarServiceImpl carService;
    private int carId = 1;

    private Car getCar(String car) {
        return new Car(carId++, "Car" + car, 1000, new Brand(), new Year(), new Body(),
                new Gearbox(), new Fuel(), new Color(), "New", new Engine(carId++, ""),
                new Owner(), Set.of(new Owner()), Set.of(new PeriodHistory()), Set.of(new Photo()));
    }

    /**
     * Создание новой записи
     */
    @Captor
    private ArgumentCaptor<Car> carArgumentCaptor;

    @Test
    public void whenCreateCarThenGetCar() {
        Car expectedCar = getCar("test1");
        when(carRepository.create(carArgumentCaptor.capture())).thenReturn(expectedCar);

        var actualCar = carService.create(expectedCar);

        assertThat(actualCar).usingRecursiveComparison().isEqualTo(expectedCar);
        assertThat(carArgumentCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedCar);
    }

    /**
     * Получение полного списка записей
     */
    @Test
    public void whenFindAllCarsThenGetAllCars() {
        Car expectedCar1 = getCar("test2");
        Car expectedCar2 = getCar("test3");
        List<Car> expectedCars = List.of(expectedCar1, expectedCar2);
        when(carRepository.findAll()).thenReturn(expectedCars);

        var actualCars = carService.findAll();

        assertThat(actualCars).usingRecursiveComparison().isEqualTo(expectedCars);
    }

    /**
     * Получение списка записей пользователя user
     */
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Test
    public void whenFindCarsUserThenGetCars() {
        Car expectedCar1 = getCar("test4");
        Car expectedCar2 = getCar("test5");
        User user = new User(1, "Name", "login", "pass", "1111");
        List<Car> expectedCars = List.of(expectedCar1, expectedCar2);
        when(carRepository.findByUser(userArgumentCaptor.capture())).thenReturn(expectedCars);

        var actualCars = carService.findByUser(user);

        assertThat(actualCars).usingRecursiveComparison().isEqualTo(expectedCars);
        assertThat(userArgumentCaptor.getValue()).usingRecursiveComparison().isEqualTo(user);
    }

    /**
     * Получение записи по id
     */
    @Captor
    private ArgumentCaptor<Integer> idArgumentCaptor;

    @Test
    public void whenFindCarByIdThenGetCars() {
        Car expectedCar = getCar("test6");
        int expectedId = 1;
        when(carRepository.findById(idArgumentCaptor.capture())).thenReturn(Optional.of(expectedCar));

        var actualCar = carService.findById(expectedId);

        assertThat(actualCar)
                .isPresent()
                        .isNotEmpty()
                                .contains(expectedCar);
        assertThat(idArgumentCaptor.getValue()).isEqualTo(expectedId);
    }

    /**
     * Удаление записи
     */
    @Test
    public void whenDeleteCarThenGetTrue() {
        Car expectedCar = getCar("test7");
        when(carRepository.delete(carArgumentCaptor.capture())).thenReturn(true);

        var actualStatus = carService.delete(expectedCar);

        assertThat(actualStatus).isTrue();
        assertThat(carArgumentCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedCar);
    }
}
