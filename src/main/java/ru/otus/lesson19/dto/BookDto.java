package ru.otus.lesson19.dto;


import lombok.*;
import ru.otus.lesson19.model.Author;
import ru.otus.lesson19.model.Genre;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private Long id;
    private String name;
    private Long author;
    private Long genre;
}
