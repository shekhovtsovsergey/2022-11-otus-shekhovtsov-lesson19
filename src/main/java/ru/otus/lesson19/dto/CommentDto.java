package ru.otus.lesson19.dto;


import lombok.*;;

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
