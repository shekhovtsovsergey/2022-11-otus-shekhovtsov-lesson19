package ru.otus.lesson19.dto;


import lombok.*;
import ru.otus.lesson19.model.Book;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String book;
    private String authorName;
    private String comment;
}
