package ru.job4j.car.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.car.model.Body;
import ru.job4j.car.repository.BodyRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BodyServiceImpl implements BodyService {
    private BodyRepository bodyRepository;

    @Override
    public List<Body> findAll() {
        return bodyRepository.findAll();
    }

    @Override
    public Optional<Body> findById(int bodyId) {
        return bodyRepository.findById(bodyId);
    }
}
