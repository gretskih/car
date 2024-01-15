package controller;

import org.junit.jupiter.api.Test;
import ru.job4j.car.controller.IndexController;

import static org.assertj.core.api.Assertions.assertThat;

public class IndexControllerTest {
    private IndexController indexController = new IndexController();

    /**
     * Получение страницы index со списком всех объявлений
     */
    @Test
    public void whenRequestGetIndexThenRedirectToPosts() {
        String expectedPage = "redirect:/posts";

        var actualPage = indexController.getIndex();

        assertThat(actualPage).isEqualTo(expectedPage);
    }
}
