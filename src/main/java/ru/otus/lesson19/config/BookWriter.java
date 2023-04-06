package ru.otus.lesson19.config;

import org.springframework.batch.item.database.JpaItemWriter;
import ru.otus.lesson19.dao.sql.AuthorDao;
import ru.otus.lesson19.dao.sql.BookDao;
import ru.otus.lesson19.dao.sql.GenreDao;
import ru.otus.lesson19.model.sql.Author;
import ru.otus.lesson19.model.sql.Book;
import ru.otus.lesson19.model.sql.Genre;
import ru.otus.lesson19.service.CacheService;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

public class BookWriter extends JpaItemWriter<Book> {
    private CacheService cacheService;
    private AuthorDao authorDao;
    private GenreDao genreDao;
    private BookDao bookDao;

    public BookWriter(EntityManagerFactory entityManagerFactory, CacheService cacheService, AuthorDao authorDao, GenreDao genreDao, BookDao bookDao) {
        setEntityManagerFactory(entityManagerFactory);
        this.cacheService = cacheService;
        this.authorDao = authorDao;
        this.genreDao = genreDao;
        this.bookDao = bookDao;
    }

    @Override
    public void write(List<? extends Book> items) {
        List<Book> booksToSave = new ArrayList<>();
        for (Book item : items) {
            Author author = cacheService.getAuthorByName(item.getAuthor().getName());
            Genre genre = cacheService.getGenreByName(item.getGenre().getName());
            if (author == null) {
                author = authorDao.findFirstByName(item.getAuthor().getName());
                if (author != null) {
                    cacheService.putAuthor(author);
                }
            }
            if (genre == null) {
                genre = genreDao.findFirstByName(item.getGenre().getName());
                if (genre != null) {
                    cacheService.putGenre(genre);
                }
            }
            item.setAuthor(author);
            item.setGenre(genre);
            booksToSave.add(item);
        }
        bookDao.saveAll(booksToSave);
    }
}