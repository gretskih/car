package ru.job4j.car.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;
    private String name;
    private String brand;
    private Integer mileage;
    private Integer year;
    private String bodyType;
    private String gearbox;
    private String fuelType;
    private String colour;
    private String type;

    /**
     * Модель двигателя
     */
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "engine_id", foreignKey = @ForeignKey(name = "ENGINE_ID_FK"))
    private Engine engine;

    /**
     * Текущий владелец автомобиля
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    /**
     * Список бывших владельцев автомобиля
     */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "auto_id")
    private Set<Owner> owners = new HashSet<>();

    /**
     * Список фото автомобиля
     */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "auto_id")
    private Set<Photo> photos = new HashSet<>();

    public int getNumberOfOwners() {
        return owners.size();
    }
}
