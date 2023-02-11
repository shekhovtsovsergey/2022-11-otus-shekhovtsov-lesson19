package ru.otus.lesson19.converter;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.lesson19.dto.CommentDto;
import ru.otus.lesson19.model.Comment;

@Component
@RequiredArgsConstructor
public class CommentConverter {

    public CommentDto entityToDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getBook().getName(),comment.getAuthorName(), comment.getComment());
    }

}
