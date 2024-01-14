package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.job4j.car.model.Color;
import ru.job4j.car.repository.ColorRepository;
import ru.job4j.car.service.ColorServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ColorServiceTest {
    @Mock
    private ColorRepository colorRepository;
    @InjectMocks
    private ColorServiceImpl colorService;

    /**
     * Получение полного списка записей
     */
    @Test
    public void whenFindAllOrderByIdThenGetListBodies() {
        Color color = new Color(1, "color");
        List<Color> expectedColorList = new ArrayList<>();
        expectedColorList.add(color);
        when(colorRepository.findAllOrderById()).thenReturn(expectedColorList);

        var actualColorList = colorService.findAllOrderById();

        assertThat(actualColorList).isEqualTo(expectedColorList);
    }

    /**
     * Поиск записи по Id
     */
    @Captor
    ArgumentCaptor<Integer> integerArgumentCaptor;

    @Test
    public void whenFindByIdThenGetColor() {
        Color expectedColor = new Color(1, "color");
        int expectedColorIdCaptor = expectedColor.getId();
        when(colorRepository.findById(integerArgumentCaptor.capture())).thenReturn(Optional.of(expectedColor));

        var actualColor = colorService.findById(expectedColorIdCaptor);
        var actualColorIdCaptor = integerArgumentCaptor.getValue();

        assertThat(actualColor)
                .isPresent()
                .isNotEmpty()
                .contains(expectedColor);
        assertThat(actualColorIdCaptor).isEqualTo(expectedColorIdCaptor);
    }
}
