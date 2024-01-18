package ru.job4j.car.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.job4j.car.model.Engine;
import ru.job4j.car.repository.EngineRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class EngineServiceImpl implements EngineService {

    private final EngineRepository engineRepository;

    @Override
    public Engine create(Engine engine) {
        try {
            return engineRepository.create(engine);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<Engine> findAll() {
        return engineRepository.findAll();
    }

    @Override
    public Optional<Engine> findById(int engineId) {
        return engineRepository.findById(engineId);
    }
}
