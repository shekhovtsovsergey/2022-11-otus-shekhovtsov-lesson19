package ru.otus.lesson19.service;

import org.springframework.stereotype.Service;
import ru.otus.lesson19.model.sql.Author;
import ru.otus.lesson19.model.sql.Genre;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CacheServiceImpl implements CacheService {

    private static final Map<String, Author> authorCache = new ConcurrentHashMap<>();
    private static final Map<String, Genre> genreCache = new ConcurrentHashMap<>();

    @Override
    public Author getAuthorByName(String name) {
        return authorCache.get(name);
    }

    @Override
    public void putAuthor(Author author) {
        authorCache.put(author.getName(), author);
    }

    @Override
    public Genre getGenreByName(String name) {
        return genreCache.get(name);
    }

    @Override
    public void putGenre(Genre genre) {
        genreCache.put(genre.getName(), genre);
    }
}