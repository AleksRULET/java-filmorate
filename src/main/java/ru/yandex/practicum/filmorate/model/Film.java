package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film implements Comparable <Film> {
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private LocalDate releaseDate;
    @Size(max = 200)
    private String description;
    @Positive
    private int duration;
    private Mpa mpa;
    private int rate;
    private Set<Genre> genres;

    public Film(Long id, String name, LocalDate releaseDate, String description, int duration, int rate)  {
        this.id = id;
        this.name = name;
        this.releaseDate = releaseDate;
        this.description = description;
        this.duration = duration;
        this.rate = rate;
    }

    public Genre addGenre(Genre genre) {
        if (genres == null) {
            genres = new HashSet<>();
        }
        Set<Long> checkSet = new HashSet<>();
        for (Genre g:getGenres()) {
            checkSet.add(g.getId());
        }
            if (!(checkSet.contains(genre.getId()))) {
                genres.add(genre);
                return genre;
            }
        return null;
    }

    @Override
    public int compareTo(Film o) {
        return (int) (this.id - o.id);
    }
}
