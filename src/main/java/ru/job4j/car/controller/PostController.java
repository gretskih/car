package ru.job4j.car.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.car.dto.PostView;
import ru.job4j.car.model.*;
import ru.job4j.car.service.BrandService;
import ru.job4j.car.service.CarService;
import ru.job4j.car.service.PostService;

import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final CarService carService;
    private final BrandService brandService;

    @GetMapping
    public String getIndex(Model model) {
        model.addAttribute("postPreviews", postService.findAllPostPreview());
        model.addAttribute("brands", brandService.findAll());
        return "index";
    }

    @PostMapping("/brand")
    public String getPagePostsOneBrand(@RequestParam int brandId, Model model) {
        model.addAttribute("postPreviews", postService.getPostsPreviewsBrandId(brandId));
        model.addAttribute("brands", brandService.findAll());
        return "index";
    }

    @GetMapping("/day")
    public String getPageLastPosts(Model model) {
        model.addAttribute("postPreviews", postService.getPostPreviewsLastDay());
        return "index";
    }

    @GetMapping("/photos")
    public String getPagePhotosPosts(Model model) {
        model.addAttribute("postPreviews", postService.getPostPreviewsWithPhoto());
        return "index";
    }

    @GetMapping("/my")
    public String getPageMyPosts(Model model, @SessionAttribute User user) {
        model.addAttribute("postPreviews", postService.getPostPreviewsUser(user));
        return "index";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model, @SessionAttribute User user) {
        model.addAttribute("cars", carService.findByUser(user));
        return "posts/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Post post, @SessionAttribute User user, @RequestParam Long price, @RequestParam Integer carId, Model model) {
        try {
            postService.create(post, user, price, carId);
        } catch (Exception e) {
            model.addAttribute("message1", e.getMessage());
            return "errors/500";
        }
        return "redirect:/posts";
    }

    @GetMapping("/{id}")
    public String getViewPage(Model model, @PathVariable int id, @SessionAttribute User user) {
        Optional<PostView> postViewOptional = postService.findById(id);
        if (postViewOptional.isEmpty()) {
            model.addAttribute("message1", "Объявление не найдено.");
            return "errors/500";
        }
        model.addAttribute("postView", postViewOptional.get());
        model.addAttribute("currentUserId", user.getId());
        return "posts/view";
    }

    @GetMapping("/complete/{id}")
    public String complete(@PathVariable int id, Model model) {
        if (!postService.changeStatus(id, true)) {
            model.addAttribute("message1", "Статус объявления не обновлен.");
            return "errors/500";
        }
        return "redirect:/posts";
    }

    @GetMapping("/place/{id}")
    public String place(@PathVariable int id, Model model) {
        if (!postService.changeStatus(id, false)) {
            model.addAttribute("message1", "Статус объявления не обновлен.");
            return "errors/500";
        }
        return "redirect:/posts";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, Model model) {
        if (!postService.delete(id)) {
            model.addAttribute("message1", "Оъявление не было удалено.");
            return "errors/500";
        }
        return "redirect:/posts";
    }
}
