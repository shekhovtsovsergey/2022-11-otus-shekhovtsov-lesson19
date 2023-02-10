package ru.otus.lesson19.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.lesson19.converter.AuthorConverter;
import ru.otus.lesson19.converter.BookConverter;
import ru.otus.lesson19.converter.CommentConverter;
import ru.otus.lesson19.converter.GenreConverter;
import ru.otus.lesson19.dao.AuthorDao;
import ru.otus.lesson19.dao.BookDao;
import ru.otus.lesson19.dao.CommentDao;
import ru.otus.lesson19.dao.GenreDao;
import ru.otus.lesson19.dto.AuthorDto;
import ru.otus.lesson19.dto.BookDto;
import ru.otus.lesson19.dto.CommentDto;
import ru.otus.lesson19.dto.GenreDto;
import ru.otus.lesson19.model.Author;
import ru.otus.lesson19.model.Book;
import ru.otus.lesson19.model.Genre;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {

    private final BookDao bookDao;
    private final CommentDao commentDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final BookConverter bookConverter;
    private final CommentConverter commentConverter;
    private final AuthorConverter authorConverter;
    private final GenreConverter genreConverter;

    @Override
    public List<BookDto> getAllBooks() {
        return bookDao.findAll().stream().map(bookConverter::entityToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteBookById(Long id) {
        commentDao.deleteByBook(new Book(id,null,null,null,null));
        bookDao.deleteById(id);
    }

    @Override
    public BookDto createBook(String name, Long authorId, Long genreId) {
        return bookConverter.entityToDto(bookDao.save(new Book(null,name, new Author(authorId,null), new Genre(genreId,null),null)));
    }

    @Override
    public BookDto updateBook(Long id, String name, Long authorId, Long genreId) {
        return bookConverter.entityToDto(bookDao.save(new Book(id,name, new Author(authorId,null), new Genre(genreId,null),null)));
    }



    @Override
    public BookDto getBookById(Long id) {
        Book book = new Book();
        if (id == null){
            return bookConverter.entityToDto(book);
        } else {
            return bookConverter.entityToDto(bookDao.findBookById(id));
        }
    }

    @Override
    public List<CommentDto> getAllCommentsByBook(Long id) {
      return  bookDao.findBookById(id).getComments().stream().map(commentConverter::entityToDto).collect(Collectors.toList());
    }

    @Override
    public List<AuthorDto> getAllAuthore() {
        return authorDao.findAll().stream().map(authorConverter::entityToDto).collect(Collectors.toList());
    }

    public List<GenreDto> getAllGenre() {
        return genreDao.findAll().stream().map(genreConverter::entityToDto).collect(Collectors.toList());
    }

    @Override
    public AuthorDto getAuthorById(Long id) {
        return authorConverter.entityToDto(authorDao.findAuthorById(id));
    }

    @Override
    public GenreDto getGenreById(Long id) {
        return genreConverter.entityToDto(genreDao.findGenreById(id));
    }

}
