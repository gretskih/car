package service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.job4j.car.model.Gearbox;
import ru.job4j.car.repository.GearboxRepository;
import ru.job4j.car.service.GearboxServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GearboxServiceTest {
    @Mock
    private GearboxRepository gearboxRepository;
    @InjectMocks
    private GearboxServiceImpl gearboxService;

    /**
     * Получение полного списка записей
     */
    @Test
    public void whenFindAllOrderByIdThenGetListBodies() {
        Gearbox gearbox = new Gearbox(1, "gearbox");
        List<Gearbox> expectedGearboxList = new ArrayList<>();
        expectedGearboxList.add(gearbox);
        when(gearboxRepository.findAll()).thenReturn(expectedGearboxList);

        var actualGearboxList = gearboxService.findAll();

        assertThat(actualGearboxList).isEqualTo(expectedGearboxList);
    }

    /**
     * Поиск записи по Id
     */
    @Captor
    ArgumentCaptor<Integer> integerArgumentCaptor;

    @Test
    public void whenFindByIdThenGetGearbox() {
        Gearbox expectedGearbox = new Gearbox(1, "gearbox");
        int expectedGearboxIdCaptor = expectedGearbox.getId();
        when(gearboxRepository.findById(integerArgumentCaptor.capture())).thenReturn(Optional.of(expectedGearbox));

        var actualGearbox = gearboxService.findById(expectedGearboxIdCaptor);
        var actualGearboxIdCaptor = integerArgumentCaptor.getValue();

        assertThat(actualGearbox)
                .isPresent()
                .isNotEmpty()
                .contains(expectedGearbox);
        assertThat(actualGearboxIdCaptor).isEqualTo(expectedGearboxIdCaptor);
    }
}
