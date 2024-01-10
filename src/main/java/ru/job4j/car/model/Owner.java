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
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String name;
}
