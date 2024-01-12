package controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CarControllerTest {
    private CarServiceImpl carService;
    private EngineServiceImpl engineService;
    private OwnerServiceImpl ownerService;
    private BrandServiceImpl brandService;
    private ColorServiceImpl colorService;
    private YearServiceImpl yearService;
    private BodyServiceImpl bodyService;
    private GearboxServiceImpl gearboxService;
    private FuelServiceImpl fuelService;
    private CarController carController;

    @BeforeEach
    public void initRepositories() {
        carService = mock(CarServiceImpl.class);
        engineService = mock(EngineServiceImpl.class);
        ownerService = mock(OwnerServiceImpl.class);
        brandService = mock(BrandServiceImpl.class);
        colorService = mock(ColorServiceImpl.class);
        yearService = mock(YearServiceImpl.class);
        bodyService = mock(BodyServiceImpl.class);
        gearboxService = mock(GearboxServiceImpl.class);
        fuelService = mock(FuelServiceImpl.class);
        carController = new CarController(carService, engineService,
                ownerService, brandService, colorService, yearService,
                bodyService, gearboxService, fuelService
        );
    }

    /**
     * Получение страницы cars/cars
     */
    @Test
    public void whenRequestGetCarsThenGetMyCarsPage() {
        String expectedPage = "cars/cars";
        User user = new User(1, "Name", "login", "pass", "1111");
        Car car = new Car(1, "Car", 1000, new Brand(), new Year(), new Body(),
                new Gearbox(), new Fuel(), new Color(), "New", new Engine(), new Owner(),
                Set.of(new Owner()), Set.of(new PeriodHistory()), Set.of(new Photo()));
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

        when(engineService.findAllOrderById()).thenReturn(List.of(engine));
        when(brandService.findAllOrderById()).thenReturn(List.of(brand));
        when(colorService.findAllOrderById()).thenReturn(List.of(color));
        when(yearService.findAllOrderById()).thenReturn(List.of(year));
        when(bodyService.findAllOrderByType()).thenReturn(List.of(body));
        when(gearboxService.findAllOrderById()).thenReturn(List.of(gearbox));
        when(fuelService.findAllOrderById()).thenReturn(List.of(fuel));

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
    public void whenCreateCarThenGetCreateNewPostPage() {
        String expectedPage = "redirect:/posts/create";
        User user = new User(1, "Name", "login", "pass", "1111");
        Car car = new Car(1, "Car", 1000, new Brand(), new Year(), new Body(),
                new Gearbox(), new Fuel(), new Color(), "New", new Engine(1, ""), new Owner(),
                Set.of(new Owner()), Set.of(new PeriodHistory()), Set.of(new Photo()));
        Engine engine = new Engine(1, "engine");
        Owner owner = Owner.of()
                .user(user)
                .name(user.getName())
                .build();
        MultipartFile multipartFile = new MockMultipartFile("File", new byte[1]);
        var engineIdCaptor = ArgumentCaptor.forClass(int.class);
        var userCaptor = ArgumentCaptor.forClass(User.class);
        var carCaptor = ArgumentCaptor.forClass(Car.class);
        ArgumentCaptor<Set<MultipartFile>> filesCaptor = ArgumentCaptor.forClass(Set.class);
        when(engineService.findById(engineIdCaptor.capture())).thenReturn(Optional.of(engine));
        when(ownerService.findByUser(userCaptor.capture())).thenReturn(Optional.of(owner));
        when(carService.create(carCaptor.capture(), filesCaptor.capture())).thenReturn(car);
        Model model = new ConcurrentModel();

        var actualPage = carController.create(car, user, Set.of(multipartFile), model);

        assertThat(actualPage).isEqualTo(expectedPage);
        assertThat(car.getEngine()).usingRecursiveComparison().isEqualTo(engine);
        assertThat(car.getOwner()).usingRecursiveComparison().isEqualTo(owner);
        assertThat(engineIdCaptor.getValue()).isEqualTo(engine.getId());
        assertThat(userCaptor.getValue()).isEqualTo(user);
        assertThat(carCaptor.getValue()).usingRecursiveComparison().isEqualTo(car);
    }

    /**
     * Неудачное cохранение нового автомобиля и редирект на страницу errors/404
     */
    @Test
    public void whenCreateCarThenGetErrorPage() {
        String expectedPage = "errors/404";
        String expectedMessage = "Автомобиль не создан!";
        User user = new User(1, "Name", "login", "pass", "1111");
        Car car = new Car(1, "Car", 1000, new Brand(), new Year(), new Body(),
                new Gearbox(), new Fuel(), new Color(), "New", new Engine(1, ""), new Owner(),
                Set.of(new Owner()), Set.of(new PeriodHistory()), Set.of(new Photo()));
        Engine engine = new Engine(1, "engine");
        MultipartFile multipartFile = new MockMultipartFile("File", new byte[1]);
        var engineIdCaptor = ArgumentCaptor.forClass(int.class);
        when(engineService.findById(engineIdCaptor.capture())).thenReturn(Optional.of(engine));
        Model model = new ConcurrentModel();

        var actualPage = carController.create(car, user, Set.of(multipartFile), model);
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
        String exceptedPage = "redirect:/cars";
        var carIdCaptor = ArgumentCaptor.forClass(int.class);
        when(carService.delete(carIdCaptor.capture())).thenReturn(true);
        Model model = new ConcurrentModel();

        var actualPage = carController.delete(carId, model);

        assertThat(actualPage).isEqualTo(exceptedPage);
        assertThat(carIdCaptor.getValue()).isEqualTo(carId);
    }

    /**
     * Неудачное удаление автомобиля и редирект на errors/404
     */
    @Test
    public void whenDeleteCarThenGetErrorPage() {
        int carId = 1;
        String exceptedPage = "errors/404";
        String exceptedMessage = "Произошла ошибка при удалении!";
        var carIdCaptor = ArgumentCaptor.forClass(int.class);
        when(carService.delete(carIdCaptor.capture())).thenReturn(false);
        Model model = new ConcurrentModel();

        var actualPage = carController.delete(carId, model);
        var actualMessage = model.getAttribute("message");

        assertThat(actualPage).isEqualTo(exceptedPage);
        assertThat(carIdCaptor.getValue()).isEqualTo(carId);
        assertThat(actualMessage).isEqualTo(exceptedMessage);
    }
}
