package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.job4j.car.model.Year;
import ru.job4j.car.repository.YearRepository;
import ru.job4j.car.service.YearServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class YearServiceTest {
    @Mock
    private YearRepository yearRepository;
    @InjectMocks
    private YearServiceImpl yearService;

    /**
     * Получение полного списка записей
     */
    @Test
    public void whenFindAllOrderByIdThenGetListBodies() {
        Year year = new Year(1, 2020);
        List<Year> expectedYearList = new ArrayList<>();
        expectedYearList.add(year);
        when(yearRepository.findAllOrderById()).thenReturn(expectedYearList);

        var actualYearList = yearService.findAllOrderById();

        assertThat(actualYearList).isEqualTo(expectedYearList);
    }

    /**
     * Поиск записи по Id
     */
    @Captor
    ArgumentCaptor<Integer> integerArgumentCaptor;

    @Test
    public void whenFindByIdThenGetYear() {
        Year expectedYear = new Year(1, 2021);
        int expectedYearIdCaptor = expectedYear.getId();
        when(yearRepository.findById(integerArgumentCaptor.capture())).thenReturn(Optional.of(expectedYear));

        var actualYear = yearService.findById(expectedYearIdCaptor);
        var actualYearIdCaptor = integerArgumentCaptor.getValue();

        assertThat(actualYear)
                .isPresent()
                .isNotEmpty()
                .contains(expectedYear);
        assertThat(actualYearIdCaptor).isEqualTo(expectedYearIdCaptor);
    }
}
