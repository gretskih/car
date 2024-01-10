package ru.job4j.car.model;

import lombok.*;

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
    private Integer mileage;
    @Column(name = "year_prod")
    private Integer yearProduction;
    private String bodyType;
    private String gearbox;
    private String fuelType;
    private String colour;
    private String type;

    /**
     * Модель двигателя
     */
    @ManyToOne
    @JoinColumn(name = "engine_id", foreignKey = @ForeignKey(name = "ENGINE_ID_FK"))
    private Engine engine;

    /**
     * Текущий владелец автомобиля
     */
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    /**
     * Список бывших владельцев автомобиля
     */
    @ManyToMany
    @JoinTable(name = "history_owner",
            joinColumns = {
                    @JoinColumn(name = "car_id", nullable = false, updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "owner_id", nullable = false, updatable = false)})
    private Set<Owner> owners = new HashSet<>();

    /**
     * Список периодов владения автомобилем
     */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "car_id")
    private Set<PeriodHistory> periodHistories = new HashSet<>();

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
