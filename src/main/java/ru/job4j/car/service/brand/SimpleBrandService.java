package ru.job4j.car.service.brand;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.car.model.Brand;
import ru.job4j.car.repository.brand.BrandRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class SimpleBrandService implements BrandService {
    private final BrandRepository brandRepository;

    /**
     * Список производителей отсортированных по id.
     * @return список производителей.
     */
    @Override
    public List<Brand> findAll() {
        return brandRepository.findAll();
    }
}
