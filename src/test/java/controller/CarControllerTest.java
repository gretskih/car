package controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.car.controller.CarController;
import ru.job4j.car.model.*;
import ru.job4j.car.service.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CarControllerTest {
    @Mock
    private CarService carService;
    @Mock
    private EngineService engineService;
    @Mock
    private BrandService brandService;
    @Mock
    private ColorService colorService;
    @Mock
    private YearService yearService;
    @Mock
    private BodyService bodyService;
    @Mock
    private GearboxService gearboxService;
    @Mock
    private FuelService fuelService;
    @Mock
    private PhotoService photoService;
    @InjectMocks
    private CarController carController;
    @Captor
    private ArgumentCaptor<Set<MultipartFile>> fileCaptor;
    @Captor
    private ArgumentCaptor<User> userCaptor;
    @Captor
    private ArgumentCaptor<Car> carCaptor;
    @Captor
    private ArgumentCaptor<Integer> carIdCaptor;

    private Car getCar() {
        return new Car(1, "Car", 1000, new Brand(), new Year(), new Body(),
                new Gearbox(), new Fuel(), new Color(), "New", new Engine(1, ""),
                new Owner(), Set.of(new Owner()), Set.of(new PeriodHistory()), Set.of(new Photo(1, "", "path")));
    }

    /**
     * Получение страницы cars/cars
     */
    @Test
    public void whenRequestGetCarsThenGetMyCarsPage() {
        String expectedPage = "cars/cars";
        User user = new User(1, "Name", "login", "pass", "1111");
        Car car = getCar();
        Model model = new ConcurrentModel();
        var captureUser = ArgumentCaptor.forClass(User.class);
        when(carService.findByUser(captureUser.capture())).thenReturn(List.of(car));

        var actualPage = carController.getMyCarsPage(model, user);
        var actualCars = model.getAttribute("cars");

        assertThat(actualPage).isEqualTo(expectedPage);
        assertThat(captureUser.getValue()).usingRecursiveComparison().isEqualTo(user);
        assertThat(actualCars).usingRecursiveComparison().isEqualTo(List.of(car));
    }

    /**
     * Получение страницы cars/create
     */
    @Test
    public void whenRequestGetCarsCreateThenGetCreationPage() {
        String expectedPage = "cars/create";
        Engine engine = new Engine(1, "engine");
        Brand brand = new Brand(1, "brand");
        Color color = new Color(1, "color");
        Year year = new Year(1, 1985);
        Body body = new Body(1, "body");
        Gearbox gearbox = new Gearbox(1, "gearbox");
        Fuel fuel = new Fuel();
        Model model = new ConcurrentModel();

        when(engineService.findAll()).thenReturn(List.of(engine));
        when(brandService.findAll()).thenReturn(List.of(brand));
        when(colorService.findAll()).thenReturn(List.of(color));
        when(yearService.findAll()).thenReturn(List.of(year));
        when(bodyService.findAll()).thenReturn(List.of(body));
        when(gearboxService.findAll()).thenReturn(List.of(gearbox));
        when(fuelService.findAll()).thenReturn(List.of(fuel));

        var actualPage = carController.getCreationPage(model);
        var actualEngines = model.getAttribute("engines");
        var actualBrands = model.getAttribute("brands");
        var actualColors = model.getAttribute("colors");
        var actualYears = model.getAttribute("years");
        var actualBodies = model.getAttribute("bodies");
        var actualGearboxes = model.getAttribute("gearboxes");
        var actualFuels = model.getAttribute("fuels");

        assertThat(actualPage).isEqualTo(expectedPage);
        assertThat(actualEngines).usingRecursiveComparison().isEqualTo(List.of(engine));
        assertThat(actualBrands).usingRecursiveComparison().isEqualTo(List.of(brand));
        assertThat(actualColors).usingRecursiveComparison().isEqualTo(List.of(color));
        assertThat(actualYears).usingRecursiveComparison().isEqualTo(List.of(year));
        assertThat(actualBodies).usingRecursiveComparison().isEqualTo(List.of(body));
        assertThat(actualGearboxes).usingRecursiveComparison().isEqualTo(List.of(gearbox));
        assertThat(actualFuels).usingRecursiveComparison().isEqualTo(List.of(fuel));
    }

    /**
     * Сохранение нового автомобиля и редирект на страницу redirect:/posts/create
     */

    @Test
    public void whenCreateCarThenGetCreateNewPostPage() throws Exception {
        String expectedPage = "redirect:/posts/create";
        User expectedUser = new User(1, "Name", "login", "pass", "1111");
        Car expectedCar = getCar();
        MultipartFile multipartFile = new MockMultipartFile("File", new byte[] {1, 2, 3});
        when(carService.create(carCaptor.capture(), userCaptor.capture(), fileCaptor.capture())).thenReturn(expectedCar);
        Model model = new ConcurrentModel();

        var actualPage = carController.create(expectedCar, expectedUser, Set.of(multipartFile), model);

        assertThat(actualPage).isEqualTo(expectedPage);
        assertThat(fileCaptor.getValue()).isEqualTo(Set.of(multipartFile));
        assertThat(userCaptor.getValue()).isEqualTo(expectedUser);
        assertThat(carCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedCar);
    }

    /**
     * Неудачное cохранение нового автомобиля и редирект на страницу errors/404
     */
    @Test
    public void whenCreateCarThenGetErrorPage() {
        String expectedPage = "errors/404";
        String expectedMessage = "Автомобиль не создан!";
        User expectedUser = new User(1, "Name", "login", "pass", "1111");
        Car expectedCar = getCar();
        MultipartFile multipartFile = new MockMultipartFile("File", new byte[]{1, 2, 3});
        Model model = new ConcurrentModel();

        var actualPage = carController.create(expectedCar, expectedUser, Set.of(multipartFile), model);
        var actualMessage = model.getAttribute("message");

        assertThat(actualPage).isEqualTo(expectedPage);
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    /**
     * Удаление автомобиля и редирект на redirect:/cars
     */

    @Test
    public void whenDeleteCarThenGetCarsPage() {
        int carId = 1;
        Car car = getCar();
        String exceptedPage = "redirect:/cars";
        when(carService.findById(carIdCaptor.capture())).thenReturn(Optional.of(car));
        when(carService.delete(carCaptor.capture())).thenReturn(true);
        Model model = new ConcurrentModel();

        var actualPage = carController.delete(carId, model);

        assertThat(actualPage).isEqualTo(exceptedPage);
        assertThat(carIdCaptor.getValue()).isEqualTo(carId);
        assertThat(carCaptor.getValue()).isEqualTo(car);
    }

    /**
     * Неудачное удаление автомобиля и редирект на errors/404
     */
    @Test
    public void whenDeleteCarThenGetErrorPage() {
        int carId = 1;
        Car car = getCar();
        String exceptedPage = "errors/404";
        String exceptedMessage = "Произошла ошибка при удалении!";
        when(carService.findById(any(Integer.class))).thenReturn(Optional.of(car));
        when(carService.delete(any(Car.class))).thenReturn(false);
        Model model = new ConcurrentModel();

        var actualPage = carController.delete(carId, model);
        var actualMessage = model.getAttribute("message");

        assertThat(actualPage).isEqualTo(exceptedPage);
        assertThat(actualMessage).isEqualTo(exceptedMessage);
    }
}
