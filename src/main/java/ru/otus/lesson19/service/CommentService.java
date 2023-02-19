package ru.otus.lesson19.service;


import ru.otus.lesson19.dto.CommentDto;
import java.util.List;

public interface CommentService {


    List<CommentDto> getAllCommentsByBook(Long id);

}
