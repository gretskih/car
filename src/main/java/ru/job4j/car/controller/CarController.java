package ru.job4j.car.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.car.model.Car;
import ru.job4j.car.service.CarService;
import ru.job4j.car.service.EngineService;

@Controller
@AllArgsConstructor
@RequestMapping("/cars")
public class CarController {
    private final CarService carService;
    private final EngineService engineService;

    @GetMapping
    public String getIndex(Model model) {
        var carPreviews = carService.findAll();
        if(carPreviews.isEmpty()) {
            model.addAttribute("message", "Объявления отсутствуют");
            return "errors/404";
        }
        model.addAttribute("carPreviews", carPreviews);
        return "index";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model) {
        model.addAttribute("engines", engineService.findAllOrderById());
        return "cars/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Car car) {
        //добавить владельца
        carService.create(car);
        return "cars/create";
    }
}
