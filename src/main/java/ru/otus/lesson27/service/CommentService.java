package ru.otus.lesson27.service;

import ru.otus.lesson27.dto.CommentDto;
import ru.otus.lesson27.exception.BookNotFoundException;
import java.util.List;

public interface CommentService {

    List<CommentDto> getAllCommentsByBook(Long id) throws BookNotFoundException;

}
