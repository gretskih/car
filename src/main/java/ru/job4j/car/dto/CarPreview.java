package ru.job4j.car.dto;

import lombok.*;
import ru.job4j.car.model.Photo;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CarPreview {
    private Set<Photo> photos;
    private String name;
    private String brand;
    private Integer mileage;
    private Integer year;
    private String bodyType;
    private String gearbox;
    private String fuelType;
    private String colour;
    private String type;
    private boolean status;

    private String engine;
    private String owner;
}
