package ru.job4j.car.model;

import lombok.*;

import javax.persistence.*;

@Data
@Builder(builderMethodName = "of")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "owner")
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;
    @Column(name = "user_id")
    private int ownerId;
    private String name;

    /**
     * Период владения автомобилем
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "history_id")
    private PeriodHistory history;
}
