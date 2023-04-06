package ru.otus.lesson19.config;

import org.springframework.batch.item.database.JpaItemWriter;
import ru.otus.lesson19.dao.sql.AuthorDao;
import ru.otus.lesson19.dao.sql.BookDao;
import ru.otus.lesson19.dao.sql.GenreDao;
import ru.otus.lesson19.model.sql.Genre;
import ru.otus.lesson19.service.CacheService;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;


public class GenreWriter extends JpaItemWriter<Genre> {
    private CacheService cacheService;
    private GenreDao genreDao;

    public GenreWriter(CacheService cacheService, GenreDao genreDao) {
        this.cacheService = cacheService;
        this.genreDao = genreDao;
    }

    @Override
    public void write(List<? extends Genre> items) {
        List<Genre> genresToSave = new ArrayList<>();
        for (Genre item : items) {
            Genre genre = cacheService.getGenreByName(item.getName());
            if (genre == null) {
                cacheService.putGenre(item);
            }
            genresToSave.add(item);
        }
        genreDao.saveAll(genresToSave);
    }
}