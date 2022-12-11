package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
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
    private Set<Integer> friends;

    public void addFriend(int id) {
        if (friends == null) {
            friends = new HashSet<>();
        }
        friends.add(id);
    }
}


