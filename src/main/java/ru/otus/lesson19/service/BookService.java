package ru.otus.lesson19.service;


import ru.otus.lesson19.dto.BookDto;
import java.util.List;

public interface BookService {

    List<BookDto> getAllBooks();
    List<BookDto> deleteBookById(Long id);
    BookDto createBook(BookDto bookDto);
    BookDto updateBook(BookDto bookDto);
    Object getBookById(Long id);

}
