package ru.job4j.car.model;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Post {
    private int id;
    private String description;
    private LocalDateTime created;
    private int userId;
}
