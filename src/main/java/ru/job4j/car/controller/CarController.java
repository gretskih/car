package ru.job4j.car.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

    @GetMapping
    public String getMyCarsPage(Model model, @SessionAttribute User user) {
        model.addAttribute("cars", carService.findByUserId(user.getId()));
        return "cars/cars";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model) {
        model.addAttribute("engines", engineService.findAllOrderById());
        return "cars/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Car car, @SessionAttribute User user, @RequestParam Set<MultipartFile> files, Model model) {
        var periodHistory = PeriodHistory.of().startAt(LocalDateTime.now()).build();
        Owner owner = Owner.of()
                .ownerId(user.getId())
                .name(user.getName())
                .history(periodHistory)
                .build();
        car.setEngine(engineService.findById(car.getEngine().getId()).get());
        car.setOwner(owner);
        car.setOwners(Set.of(owner));
        if (carService.create(car, files) == null) {
            model.addAttribute("message", "Автомобиль не создан!");
            return "errors/404";
        }
        return "redirect:/posts/create";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam int carId) {
        carService.delete(carId);
        return "redirect:/cars/cars";
    }
}
