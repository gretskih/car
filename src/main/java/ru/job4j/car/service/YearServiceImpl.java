package ru.job4j.car.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.car.model.Year;
import ru.job4j.car.repository.YearRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class YearServiceImpl implements YearService {
    private YearRepository yearRepository;

    /**
     * Список лет отсортированных по id.
     * @return список лет.
     */
    @Override
    public List<Year> findAllOrderById() {
        return yearRepository.findAllOrderById();
    }

    /**
     * Найти год по ID
     * @return Optional or year.
     */
    @Override
    public Optional<Year> findById(int yearId) {
        return yearRepository.findById(yearId);
    }
}
