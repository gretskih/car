package ru.job4j.car.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder(builderMethodName = "of")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "history")
public class PeriodHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
}
