package ru.job4j.car.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostDto {
    private int id;
    private String description;
    private LocalDateTime createdDate;
    private int userId;
}
