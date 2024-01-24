package ru.job4j.car.service.fuel;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.car.model.Fuel;
import ru.job4j.car.repository.fuel.FuelRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleFuelService implements FuelService {
    private final FuelRepository fuelRepository;

    /**
     * Список типов топлива отсортированных по id.
     * @return список типов топлива.
     */
    @Override
    public List<Fuel> findAll() {
        return fuelRepository.findAll();
    }

    /**
     * Найти топливо по ID
     * @return Optional of fuel.
     */
    @Override
    public Optional<Fuel> findById(int fuelId) {
        return fuelRepository.findById(fuelId);
    }
}
