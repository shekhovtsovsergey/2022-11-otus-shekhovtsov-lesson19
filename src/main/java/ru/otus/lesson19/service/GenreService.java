package ru.otus.lesson19.service;


import ru.otus.lesson19.dto.GenreDto;
import java.util.List;

public interface GenreService {

    List<GenreDto> getAllGenre();
    Object getGenreById(Long id);


}
