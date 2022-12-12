package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class Film implements Comparable<Film> {
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private LocalDate releaseDate;
    @Size(max = 200)
    private String description;
    @Positive
    private int duration;
    private int rate;
    private Set<Long> likes;

    public void like(Long id) {
        if (likes == null) {
            likes = new HashSet<>();
        }
        likes.add(id);
    }

    public void removeLike(Long id) {
        if (likes != null) {
            if (likes.contains(id)) {
                likes.remove(id);
                return;
            } else {
                throw new ObjectNotFoundException("Лайк не найден");
            }
        }
        throw new ObjectNotFoundException("Лайков нет");
    }

    @Override
    public int compareTo(Film o) {
        if (o.getLikes().isEmpty() || likes.isEmpty()) {
            if (likes.isEmpty()) {
                return 0;
            }
            return 1;
        }
        return this.likes.size() - o.getLikes().size();
    }
}
