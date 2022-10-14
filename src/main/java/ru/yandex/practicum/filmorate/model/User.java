package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
public class User {
    @NotBlank
    private String login;
    private String name;
    private int id;
    @NotBlank
    @Email
    private String email;
    @NotNull
    @PastOrPresent
    private LocalDate birthday;

    public User(String login, String name, int id, String email, LocalDate birthday) {
        this.login = login;
        this.name = name;
        this.id = id;
        this.email = email;
        this.birthday = birthday;
    }
}
