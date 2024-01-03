package ru.job4j.car.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class PhotoDto {
    private String name;
    private byte[] content;
}
