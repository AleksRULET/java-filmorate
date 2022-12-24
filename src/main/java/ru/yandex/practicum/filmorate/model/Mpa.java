package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Mpa {
    private Long id;
    private String name;
    public int hashCode() {
        int prime = 31;
        return prime* Objects.hash(id, name);
    }
    @Override
    public String toString() {
        return String.format("Mpa(id=&d, name=&s)", id, name);
    }
}
