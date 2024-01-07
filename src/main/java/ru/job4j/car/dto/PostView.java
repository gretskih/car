package ru.job4j.car.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.job4j.car.model.Photo;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostView {
    private int id;
    private int userId;
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
    private int numberOfOwners;
    private String contact;

    private String price;

    private String description;
    private LocalDateTime created;
    private String city;

}
