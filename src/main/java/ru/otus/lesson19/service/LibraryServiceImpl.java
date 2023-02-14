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
import java.util.Optional;
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
    @Transactional
    public BookDto createBook(BookDto bookDto) {
        Author author = authorDao.findAuthorById(bookDto.getAuthor());
        Genre genre = genreDao.findGenreById(bookDto.getGenre());
        Book book = new Book(null, bookDto.getName(), author, genre, null);
        return bookConverter.entityToDto(bookDao.save(book));
    }

    @Override
    @Transactional
    public BookDto updateBook(BookDto bookDto) {
        Author author = authorDao.findAuthorById(bookDto.getAuthor());
        Genre genre = genreDao.findGenreById(bookDto.getGenre());
        Book book = new Book(bookDto.getId(), bookDto.getName(), author, genre, null);
        authorDao.save(author);
        genreDao.save(genre);
        bookDao.save(book);
        return bookConverter.entityToDto(bookDao.findBookById(bookDto.getId()));
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
        System.out.println(bookDao.findBookById(id).getComments());
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
