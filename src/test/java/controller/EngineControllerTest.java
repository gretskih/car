package controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.car.controller.EngineController;
import ru.job4j.car.model.Engine;
import ru.job4j.car.service.engine.EngineService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EngineControllerTest {
    @Mock
    private EngineService engineService;
    @InjectMocks
    private EngineController engineController;
    @Captor
    private ArgumentCaptor<Engine> captureEngine;

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
    public void whenPostEngineThenGetCreateNewCarPage() throws Exception {
        String expectedPage = "redirect:/cars/create";
        Engine expectedEngine = new Engine(1, "Седан");
        var model = new ConcurrentModel();
        when(engineService.create(captureEngine.capture())).thenReturn(expectedEngine);

        var actualPage = engineController.create(expectedEngine, model);

        assertThat(actualPage).isEqualTo(expectedPage);
        assertThat(captureEngine.getValue()).usingRecursiveComparison().isEqualTo(expectedEngine);
    }

    /**
     * Неудачное сохранение нового двигателя в базу и редирект на страницу errors/500
     */
    @Test
    public void whenPostEngineThenGetErrorPage() throws Exception {
        String expectedPage = "errors/500";
        String expectedMessage = "Ошибка при добавлении двигателя.";
        Engine expectedEngine = new Engine(1, "Седан");
        when(engineService.create(any(Engine.class))).thenThrow(new RuntimeException("Ошибка при добавлении двигателя."));
        var model = new ConcurrentModel();

        var actualPage = engineController.create(expectedEngine, model);
        var actualMessage = model.getAttribute("message1");

        assertThat(actualPage).isEqualTo(expectedPage);
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }
}
