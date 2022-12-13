package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;

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
    private Long id;
    @NotBlank
    @Email
    private String email;
    @NotNull
    @PastOrPresent
    private LocalDate birthday;
    private Set<Long> friends;

    public void addFriend(Long id) {
        if (friends == null) {
            friends = new HashSet<>();
        }
        friends.add(id);
    }

    public void deleteFriend(Long id){
        if (friends != null) {
            if (friends.contains(id)) {
                friends.remove(id);
                return;
            } else {
                throw new ObjectNotFoundException("Друг не найден");
            }
        }
        throw new ObjectNotFoundException("Друзей нет");
    }
}


