package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Genre {
    private Long id;
    private String name;



    @Override
    public int hashCode() {
        int prime = 31;
        return prime* Objects.hash(id, name);
    }
}
