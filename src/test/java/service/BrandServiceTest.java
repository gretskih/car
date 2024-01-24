package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.job4j.car.model.Brand;
import ru.job4j.car.repository.brand.BrandRepository;
import ru.job4j.car.service.brand.SimpleBrandService;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BrandServiceTest {
    @Mock
    private BrandRepository brandRepository;
    @InjectMocks
    private SimpleBrandService brandService;

    /**
     * Получение полного списка записей
     */
    @Test
    public void whenFindAllOrderByTypeThenGetListBodies() {
        Brand brand = new Brand(1, "Brand");
        List<Brand> expectedBrandList = new ArrayList<>();
        expectedBrandList.add(brand);
        when(brandRepository.findAll()).thenReturn(expectedBrandList);

        var actualBrandList = brandService.findAll();

        assertThat(actualBrandList).isEqualTo(expectedBrandList);
    }
}
