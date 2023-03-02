package ru.otus.lesson19.exception;

public class GenreNotFoundException extends ObjectNotFoundException {

    public GenreNotFoundException(Long genreId) {
        super(String.format("Genre with id %s not found", genreId));
    }
}
