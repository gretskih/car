package ru.job4j.car.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.car.model.Engine;
import ru.job4j.car.repository.EngineRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EngineServiceImpl implements EngineService {

    private final EngineRepository engineRepository;

    @Override
    public Engine create(Engine engine) {
        return engineRepository.create(engine);
    }

    @Override
    public List<Engine> findAllOrderById() {
        return engineRepository.findAllOrderById();
    }

    @Override
    public Optional<Engine> findById(int engineId) {
        return engineRepository.findById(engineId);
    }
}
