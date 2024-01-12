package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ru.job4j.car.model.Engine;
import ru.job4j.car.repository.EngineRepositoryImpl;
import ru.job4j.car.service.EngineServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EngineServiceTest {
    private EngineRepositoryImpl engineRepository;
    private EngineServiceImpl engineService;

    @BeforeEach
    public void initRepositories() {
        engineRepository = mock(EngineRepositoryImpl.class);
        engineService = new EngineServiceImpl(engineRepository);
    }

    /**
     * Создание новой записи
     */
    @Test
    public void whenCreateEngineThenGetReturnEngine() {
        Engine expectedEngine = new Engine(1, "Test");
        var engineCaptor = ArgumentCaptor.forClass(Engine.class);
        when(engineRepository.create(engineCaptor.capture())).thenReturn(expectedEngine);

        var actualEngine = engineService.create(expectedEngine);
        var actualEngineCaptor = engineCaptor.getValue();

        assertThat(actualEngine).usingRecursiveComparison().isEqualTo(expectedEngine);
        assertThat(actualEngineCaptor).usingRecursiveComparison().isEqualTo(expectedEngine);
    }


    /**
     * Получение полного списка записей
     */
    @Test
    public void whenFindAllOrderByTypeThenGetListEngines() {
        Engine engine = new Engine(1, "Test");
        List<Engine> expectedEngineList = new ArrayList<>();
        expectedEngineList.add(engine);
        when(engineRepository.findAllOrderById()).thenReturn(expectedEngineList);

        var actualEngineList = engineService.findAllOrderById();

        assertThat(actualEngineList).isEqualTo(expectedEngineList);
    }

    /**
     * Поиск записи по Id
     */
    @Test
    public void whenFindByIdThenGetEngine() {
        Engine expectedEngine = new Engine(1, "Test");
        var engineIdCaptor = ArgumentCaptor.forClass(int.class);
        int expectedEngineIdCaptor = expectedEngine.getId();
        when(engineRepository.findById(engineIdCaptor.capture())).thenReturn(Optional.of(expectedEngine));

        var actualEngine = engineService.findById(expectedEngineIdCaptor);
        var actualEngineIdCaptor = engineIdCaptor.getValue();

        assertThat(actualEngine)
                .isPresent()
                .isNotEmpty()
                .contains(expectedEngine);
        assertThat(actualEngineIdCaptor).isEqualTo(expectedEngineIdCaptor);
    }
}
