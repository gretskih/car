package ru.job4j.car.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "auto_post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;
    private String description;
    private LocalDateTime created;

    /**
     * Владелец поста
     */
    @ManyToOne
    @JoinColumn(name = "auto_user_id")
    private User user;

    /**
     * Изменение цены
     */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "auto_post_id")
    private List<PriceHistory> priceHistories = new ArrayList<>();

    /**
     * Подписчики
     */
    @ManyToMany
    @JoinTable(
            name = "participates",
            joinColumns =  @JoinColumn(name = "post_id"),
            inverseJoinColumns =  @JoinColumn(name = "user_id")
    )
    private List<User> participates = new ArrayList<>();

    /**
     * Автомобиль в продаже
     */
    @OneToOne
    @JoinColumn(name = "car_id")
    private Car car;
}
