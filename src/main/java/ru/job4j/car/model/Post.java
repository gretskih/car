package ru.job4j.car.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private String city;
    private boolean status;

    /**
     * Владелец поста
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "auto_user_id")
    private User user;

    /**
     * Изменение цены
     */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "auto_post_id")
    private Set<PriceHistory> priceHistories = new HashSet<>();

    /**
     * Подписчики
     */
    @ManyToMany
    @JoinTable(
            name = "participates",
            joinColumns =  @JoinColumn(name = "post_id"),
            inverseJoinColumns =  @JoinColumn(name = "user_id")
    )
    private Set<User> participates = new HashSet<>();

    /**
     * Автомобиль в продаже
     */
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "car_id")
    private Car car;
}
