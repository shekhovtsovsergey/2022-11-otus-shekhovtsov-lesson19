package ru.otus.lesson19.service;


import ru.otus.lesson19.dto.AuthorDto;
import ru.otus.lesson19.dto.BookDto;
import ru.otus.lesson19.dto.CommentDto;
import ru.otus.lesson19.dto.GenreDto;
import ru.otus.lesson19.model.Author;
import ru.otus.lesson19.model.Book;
import ru.otus.lesson19.model.Genre;
import java.util.List;
import java.util.Optional;

public interface LibraryService {

    List<BookDto> getAllBooks();
    void deleteBookById(Long id);
    BookDto createBook(String name, Long authorId, Long genreId);
    BookDto updateBook(Long id, String name, Long authorId, Long genreId);
    BookDto getBookById(Long id);
    List<CommentDto> getAllCommentsByBook(Long id);
    List<AuthorDto> getAllAuthore();
    List<GenreDto> getAllGenre();
    AuthorDto getAuthorById(Long id);
    GenreDto getGenreById(Long id);

}
