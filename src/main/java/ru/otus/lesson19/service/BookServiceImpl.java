package ru.otus.lesson19.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.lesson19.converter.BookConverter;
import ru.otus.lesson19.dao.AuthorDao;
import ru.otus.lesson19.dao.BookDao;
import ru.otus.lesson19.dao.CommentDao;
import ru.otus.lesson19.dao.GenreDao;
import ru.otus.lesson19.dto.BookDto;
import ru.otus.lesson19.exception.BookNotFoundException;
import ru.otus.lesson19.model.Author;
import ru.otus.lesson19.model.Book;
import ru.otus.lesson19.model.Genre;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService{


    private final BookDao bookDao;
    private final CommentDao commentDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final BookConverter bookConverter;

    @Override
    public List<BookDto> getAllBooks() {
        return bookDao.findAll().stream().map(bookConverter::entityToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<BookDto> deleteBookById(Long id) {
        commentDao.deleteByBook(new Book(id,null, null,null,null));
        bookDao.deleteById(id);
        return bookDao.findAll().stream().map(bookConverter::entityToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookDto createBook(BookDto bookDto) {
        Author author = authorDao.findById(bookDto.getAuthor()).orElse(null);
        Genre genre = genreDao.findById(bookDto.getGenre()).orElse(null);
        Book book = new Book(null, bookDto.getName(), author, genre, null);
        return bookConverter.entityToDto(bookDao.save(book));
    }

    @Override
    @Transactional
    public BookDto updateBook(BookDto bookDto){
        Author author = authorDao.findById(bookDto.getAuthor()).orElse(null);
        Genre genre = genreDao.findById(bookDto.getGenre()).orElse(null);
        Book book = new Book(bookDto.getId(), bookDto.getName(), author, genre, null);
        return bookConverter.entityToDto(bookDao.save(book));
    }


    @Override
    public BookDto getBookById(Long id) throws BookNotFoundException {
        return bookConverter.entityToDto(bookDao.findById(id).orElseThrow(() -> new BookNotFoundException(id)));
    }
}
