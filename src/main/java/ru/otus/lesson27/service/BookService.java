package ru.otus.lesson27.service;

import ru.otus.lesson27.dto.BookDto;
import ru.otus.lesson27.exception.AuthorNotFoundException;
import ru.otus.lesson27.exception.BookNotFoundException;
import ru.otus.lesson27.exception.GenreNotFoundException;

import java.util.List;

public interface BookService {

    List<BookDto> getAllBooks();
    List<BookDto> deleteBookById(Long id);
    BookDto createBook(BookDto bookDto) throws AuthorNotFoundException, GenreNotFoundException;
    BookDto updateBook(BookDto bookDto) throws AuthorNotFoundException, GenreNotFoundException;
    BookDto getBookById(Long id) throws BookNotFoundException;

}
