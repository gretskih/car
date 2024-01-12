package controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.car.controller.EngineController;
import ru.job4j.car.model.Engine;
import ru.job4j.car.service.EngineService;
import ru.job4j.car.service.EngineServiceImpl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EngineControllerTest {
    private EngineService engineService;
    private EngineController engineController;

    @BeforeEach
    public void initRepositories() {
        engineService = mock(EngineServiceImpl.class);
        engineController = new EngineController(engineService);
    }

    /**
     * Получение страницы engines/create
     */
    @Test
    public void whenRequestGetCreationPageThenGetCreateNewEnginePage() {
        String expectedPage = "engines/create";
        var actualPage = engineController.getCreationPage();

        assertThat(actualPage).isEqualTo(expectedPage);
    }

    /**
     * Сохранение нового двигателя в базу и редирект на страницу /cars/create
     */
    @Test
    public void whenPostEngineThenGetCreateNewCarPage() {
        String expectedPage = "redirect:/cars/create";
        Engine expectedEngine = new Engine(1, "Седан");
        var model = new ConcurrentModel();
        var captureEngine = ArgumentCaptor.forClass(Engine.class);
        when(engineService.create(captureEngine.capture())).thenReturn(expectedEngine);

        var actualPage = engineController.create(expectedEngine, model);

        assertThat(actualPage).isEqualTo(expectedPage);
        assertThat(captureEngine.getValue()).usingRecursiveComparison().isEqualTo(expectedEngine);
    }

    /**
     * Неудачное сохранение нового двигателя в базу и редирект на страницу errors/404
     */
    @Test
    public void whenPostEngineThenGetErrorPage() {
        String expectedPage = "errors/404";
        String expectedMessage = "Двигатель не создан!";
        Engine expectedEngine = new Engine(1, "Седан");
        var model = new ConcurrentModel();
        var captureEngine = ArgumentCaptor.forClass(Engine.class);
        when(engineService.create(captureEngine.capture())).thenReturn(null);

        var actualPage = engineController.create(expectedEngine, model);
        var actualMessage = model.getAttribute("message");

        assertThat(actualPage).isEqualTo(expectedPage);
        assertThat(captureEngine.getValue()).usingRecursiveComparison().isEqualTo(expectedEngine);
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }
}
