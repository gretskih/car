package ru.job4j.car.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    /**
     * Модель двигателя
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
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
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "history_owner",
            joinColumns = {
                @JoinColumn(name = "car_id", nullable = false, updatable = false)},
            inverseJoinColumns = {
                @JoinColumn(name = "owner_id", nullable = false, updatable = false)})
    private Set<Owner> owners = new HashSet<>();

    /**
     * Список фото автомобиля
     */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "auto_id")
    private Set<Photo> photos = new HashSet<>();
}
