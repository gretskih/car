package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.job4j.car.model.Fuel;
import ru.job4j.car.repository.FuelRepository;
import ru.job4j.car.service.FuelServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FuelServiceTest {
    @Mock
    private FuelRepository fuelRepository;
    @InjectMocks
    private FuelServiceImpl fuelService;

    /**
     * Получение полного списка записей
     */
    @Test
    public void whenFindAllOrderByIdThenGetListBodies() {
        Fuel fuel = new Fuel(1, "fuel");
        List<Fuel> expectedFuelList = new ArrayList<>();
        expectedFuelList.add(fuel);
        when(fuelRepository.findAllOrderById()).thenReturn(expectedFuelList);

        var actualFuelList = fuelService.findAllOrderById();

        assertThat(actualFuelList).isEqualTo(expectedFuelList);
    }

    /**
     * Поиск записи по Id
     */
    @Captor
    ArgumentCaptor<Integer> integerArgumentCaptor;

    @Test
    public void whenFindByIdThenGetFuel() {
        Fuel expectedFuel = new Fuel(1, "fuel");
        int expectedFuelIdCaptor = expectedFuel.getId();
        when(fuelRepository.findById(integerArgumentCaptor.capture())).thenReturn(Optional.of(expectedFuel));

        var actualFuel = fuelService.findById(expectedFuelIdCaptor);
        var actualFuelIdCaptor = integerArgumentCaptor.getValue();

        assertThat(actualFuel)
                .isPresent()
                .isNotEmpty()
                .contains(expectedFuel);
        assertThat(actualFuelIdCaptor).isEqualTo(expectedFuelIdCaptor);
    }
}
