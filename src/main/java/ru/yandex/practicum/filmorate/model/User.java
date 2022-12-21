package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;

@Data
@AllArgsConstructor
public class User {
    private Long id;
    private String login;
    private String name;
    @NotBlank
    @Email
    private String email;
    @NotNull
    @PastOrPresent
    private LocalDate birthday;
}


