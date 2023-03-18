package ru.otus.lesson27.service;

import ru.otus.lesson27.dto.GenreDto;
import ru.otus.lesson27.exception.GenreNotFoundException;
import java.util.List;

public interface GenreService {

    List<GenreDto> getAllGenre() throws GenreNotFoundException;
    GenreDto getGenreById(Long id) throws GenreNotFoundException;


}
