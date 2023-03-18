package ru.otus.lesson27.dto;


import lombok.*;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenreDto {
    private Long id;
    private String name;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass() && getClass() != o.getClass().getSuperclass()) return false;
        GenreDto genreDto = (GenreDto) o;
        return id.equals(genreDto.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,name);
    }


}
