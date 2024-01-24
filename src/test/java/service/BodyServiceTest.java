package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.job4j.car.model.Body;
import ru.job4j.car.repository.body.BodyRepository;
import ru.job4j.car.service.body.SimpleBodyService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BodyServiceTest {
    @Mock
    private BodyRepository bodyRepository;
    @InjectMocks
    private SimpleBodyService bodyService;

    /**
     * Получение полного списка записей
     */
    @Test
    public void whenFindAllOrderByTypeThenGetListBodies() {
        Body body = new Body(1, "Седан");
        List<Body> expectedBodyList = new ArrayList<>();
        expectedBodyList.add(body);
        when(bodyRepository.findAll()).thenReturn(expectedBodyList);

        var actualBodyList = bodyService.findAll();

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
