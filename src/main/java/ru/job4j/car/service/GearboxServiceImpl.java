package ru.job4j.car.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.car.model.Gearbox;
import ru.job4j.car.repository.GearboxRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GearboxServiceImpl implements GearboxService {
    private final GearboxRepository gearboxRepository;

    /**
     * Список коробок отсортированных по id.
     * @return список коробок.
     */
    @Override
    public List<Gearbox> findAllOrderById() {
        return gearboxRepository.findAllOrderById();
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
