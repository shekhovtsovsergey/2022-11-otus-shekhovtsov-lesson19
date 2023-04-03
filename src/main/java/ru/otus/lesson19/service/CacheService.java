package ru.otus.lesson19.service;

import ru.otus.lesson19.model.sql.Author;
import ru.otus.lesson19.model.sql.Genre;

public interface CacheService {
    Author getAuthorByName(String name);
    void putAuthor(Author author);
    Genre getGenreByName(String name);
    void putGenre(Genre genre);
}