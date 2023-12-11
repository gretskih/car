package ru.job4j.car.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDto {
    private int id;
    private String loginDto;
    private String password;
}
