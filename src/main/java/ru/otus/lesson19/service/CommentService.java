package ru.otus.lesson19.service;

import ru.otus.lesson19.dto.CommentDto;
import ru.otus.lesson19.exception.BookNotFoundException;
import java.util.List;

public interface CommentService {

    List<CommentDto> getAllCommentsByBook(Long id) throws BookNotFoundException;

}
