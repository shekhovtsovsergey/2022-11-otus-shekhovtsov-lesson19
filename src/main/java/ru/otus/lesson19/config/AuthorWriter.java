package ru.otus.lesson19.config;

import org.springframework.batch.item.database.JpaItemWriter;
import ru.otus.lesson19.dao.sql.AuthorDao;
import ru.otus.lesson19.model.sql.Author;
import ru.otus.lesson19.service.CacheService;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

public class AuthorWriter extends JpaItemWriter<Author> {
    private final CacheService cacheService;
    private final AuthorDao authorDao;
    public AuthorWriter(CacheService cacheService, AuthorDao authorDao, EntityManagerFactory entityManagerFactory) {
        super();
        setEntityManagerFactory(entityManagerFactory);
        this.cacheService = cacheService;
        this.authorDao = authorDao;
    }
    @Override
    public void write(List<? extends Author> items) {
        List<Author> authorsToSave = new ArrayList<>();
        for (Author item : items) {
            Author author = cacheService.getAuthorByName(item.getName());
            if (author == null) {
                cacheService.putAuthor(item);
            }
            authorsToSave.add(item);
        }
        authorDao.saveAll(authorsToSave);
    }
}