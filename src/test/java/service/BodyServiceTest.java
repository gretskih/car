package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ru.job4j.car.model.Body;
import ru.job4j.car.repository.BodyRepositoryImpl;
import ru.job4j.car.service.BodyServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BodyServiceTest {
    private BodyRepositoryImpl bodyRepository;
    private BodyServiceImpl bodyService;

    @BeforeEach
    public void initRepositories() {
        bodyRepository = mock(BodyRepositoryImpl.class);
        bodyService = new BodyServiceImpl(bodyRepository);
    }

    /**
     * Получение полного списка записей
     */
    @Test
    public void whenFindAllOrderByTypeThenGetListBodies() {
        Body body = new Body(1, "Седан");
        List<Body> expectedBodyList = new ArrayList<>();
        expectedBodyList.add(body);
        when(bodyRepository.findAllOrderByType()).thenReturn(expectedBodyList);

        var actualBodyList = bodyService.findAllOrderByType();

        assertThat(actualBodyList).isEqualTo(expectedBodyList);
    }

    /**
     * Поиск записи по Id
     */
    @Test
    public void whenFindByIdThenGetBody() {
        Body expectedBody = new Body(1, "Седан");
        var bodyIdCaptor = ArgumentCaptor.forClass(int.class);
        int expectedBodyIdCaptor = expectedBody.getId();
        when(bodyRepository.findById(bodyIdCaptor.capture())).thenReturn(Optional.of(expectedBody));

        var actualBody = bodyService.findById(expectedBodyIdCaptor);
        var actualBodyIdCaptor = bodyIdCaptor.getValue();

        assertThat(actualBody)
                .isPresent()
                .isNotEmpty()
                .contains(expectedBody);
        assertThat(actualBodyIdCaptor).isEqualTo(expectedBodyIdCaptor);
    }
}
