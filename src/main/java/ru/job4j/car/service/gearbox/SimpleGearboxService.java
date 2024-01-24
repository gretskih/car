package ru.job4j.car.service.gearbox;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.car.model.Gearbox;
import ru.job4j.car.repository.gearbox.GearboxRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleGearboxService implements GearboxService {
    private final GearboxRepository gearboxRepository;

    /**
     * Список коробок отсортированных по id.
     * @return список коробок.
     */
    @Override
    public List<Gearbox> findAll() {
        return gearboxRepository.findAll();
    }

    /**
     * Найти коробку по ID
     * @return Optional or gearbox.
     */
    @Override
    public Optional<Gearbox> findById(int gearboxId) {
        return gearboxRepository.findById(gearboxId);
    }
}
