package ru.job4j.car.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.car.dto.PhotoDto;
import ru.job4j.car.model.Car;
import ru.job4j.car.model.Owner;
import ru.job4j.car.model.PeriodHistory;
import ru.job4j.car.model.User;
import ru.job4j.car.service.CarService;
import ru.job4j.car.service.EngineService;

import java.time.LocalDateTime;
import java.util.Set;

@Controller
@AllArgsConstructor
@RequestMapping("/cars")
public class CarController {
    private final CarService carService;
    private final EngineService engineService;

    @GetMapping("/create")
    public String getCreationPage(Model model) {
        model.addAttribute("engines", engineService.findAllOrderById());
        return "cars/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Car car, @SessionAttribute User user, @RequestParam Set<MultipartFile> files, Model model) {
        car.setEngine(engineService.findById(car.getEngine().getId()).get());
        PeriodHistory periodHistory = new PeriodHistory();
        periodHistory.setStartAt(LocalDateTime.now());
        Owner owner = new Owner();
        owner.setName(user.getName());
        owner.setHistory(periodHistory);
        car.setOwner(owner);
        try {
            carService.create(car, files);
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }
        return "posts/create";
    }
}
