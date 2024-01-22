package ru.job4j.car.dto;

import lombok.*;
import ru.job4j.car.model.Photo;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostPreview {
    private int id;
    private Set<Photo> photos;
    private String name;
    private String brand;
    private Integer mileage;
    private Integer year;
    private String bodyType;
    private String gearbox;
    private String fuelType;
    private String color;
    private String type;
    private boolean status;

    private String engine;
    private String owner;

    private Long price;
}
