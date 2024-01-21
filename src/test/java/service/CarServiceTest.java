package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.car.model.*;
import ru.job4j.car.repository.CarRepository;
import ru.job4j.car.service.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {
    @Mock
    private CarRepository carRepository;
    @Mock
    private EngineService engineService;
    @Mock
    private OwnerService ownerService;
    @Mock
    private PhotoService photoService;
    @InjectMocks
    private CarServiceImpl carService;

    @Captor
    private ArgumentCaptor<Car> carArgumentCaptor;
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;
    @Captor
    private ArgumentCaptor<Set<MultipartFile>> filesArgumentCaptor;
    @Captor
    private ArgumentCaptor<Integer> idArgumentCaptor;
    private int carId = 1;

    private Car getCar(String car) {
        return new Car(carId++, "Car" + car, 1000, new Brand(), new Year(), new Body(),
                new Gearbox(), new Fuel(), new Color(), "New", new Engine(carId++, ""),
                new Owner(), Set.of(new Owner()), Set.of(new PeriodHistory()), Set.of(new Photo()));
    }

    /**
     * Создание новой записи
     */

    @Test
    public void whenCreateCarThenGetCar() throws Exception {
        Car expectedCar = getCar("test1");
        User expectedUser = new User(1, "user", "login", "pass", "0000");
        MultipartFile file = new MockMultipartFile("file", new byte[]{1, 2, 3});
        Owner expectedOwner = Owner.of()
                .user(expectedUser)
                .name(expectedUser.getName())
                .build();
        int expectedEngineId = expectedCar.getEngine().getId();
        Engine expectedEngine = new Engine(expectedCar.getEngine().getId(), "engine");
        when(carRepository.create(carArgumentCaptor.capture())).thenReturn(expectedCar);
        when(photoService.savePhotos(filesArgumentCaptor.capture())).thenReturn(Set.of(new Photo()));
        when(ownerService.create(userArgumentCaptor.capture())).thenReturn(expectedOwner);
        when(engineService.findById(idArgumentCaptor.capture())).thenReturn(Optional.of(expectedEngine));

        var actualCar = carService.create(expectedCar, expectedUser, Set.of(file));

        assertThat(actualCar).usingRecursiveComparison().isEqualTo(expectedCar);
        assertThat(carArgumentCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedCar);
        assertThat(filesArgumentCaptor.getValue()).isEqualTo(Set.of(file));
        assertThat(userArgumentCaptor.getValue()).isEqualTo(expectedUser);
        assertThat(idArgumentCaptor.getValue()).isEqualTo(expectedEngineId);
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
    @Test
    public void whenFindCarsUserThenGetCars() {
        Car expectedCar1 = getCar("test4");
        Car expectedCar2 = getCar("test5");
        User user = new User(1, "Name", "login", "pass", "1111");
        List<Car> expectedCars = List.of(expectedCar1, expectedCar2);
        when(carRepository.findByUserId(idArgumentCaptor.capture())).thenReturn(expectedCars);

        var actualCars = carService.findByUser(user);

        assertThat(actualCars).usingRecursiveComparison().isEqualTo(expectedCars);
        assertThat(idArgumentCaptor.getValue()).usingRecursiveComparison().isEqualTo(user.getId());
    }

    /**
     * Получение записи по id
     */
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
