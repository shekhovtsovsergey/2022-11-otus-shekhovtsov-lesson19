package ru.otus.lesson27.exception;

public class GenreNotFoundException extends ObjectNotFoundException {

    public GenreNotFoundException(Long genreId) {
        super(String.format("Genre id %s not found", genreId));
    }

    public GenreNotFoundException(String message) {
        super(message);
    }
}
