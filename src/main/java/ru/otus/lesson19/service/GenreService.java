package ru.otus.lesson19.service;

import ru.otus.lesson19.dto.GenreDto;
import ru.otus.lesson19.exception.GenreNotFoundException;
import java.util.List;

public interface GenreService {

    List<GenreDto> getAllGenre() throws GenreNotFoundException;
    GenreDto getGenreById(Long id) throws GenreNotFoundException;


}
