package ru.job4j.car.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.car.model.Engine;
import ru.job4j.car.service.EngineService;

@Controller
@AllArgsConstructor
@RequestMapping("/engines")
public class EngineController {

    private final EngineService engineService;

    @GetMapping("/create")
    public String getCreationPage() {
        return "engines/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Engine engine, Model model) {
        if (engineService.create(engine) == null) {
            model.addAttribute("message", "Двигатель не создан!");
            return "errors/404";
        }
        return "redirect:/cars/create";
    }
}
