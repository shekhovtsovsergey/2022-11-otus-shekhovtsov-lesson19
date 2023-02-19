package ru.otus.lesson19.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.lesson19.converter.CommentConverter;
import ru.otus.lesson19.dao.BookDao;
import ru.otus.lesson19.dto.CommentDto;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService{

    private final BookDao bookDao;
    private final CommentConverter commentConverter;


    @Override
    public List<CommentDto> getAllCommentsByBook(Long id) {
        return  bookDao.findById(id).orElse(null).getComments().stream().map(commentConverter::entityToDto).collect(Collectors.toList());
    }


}
