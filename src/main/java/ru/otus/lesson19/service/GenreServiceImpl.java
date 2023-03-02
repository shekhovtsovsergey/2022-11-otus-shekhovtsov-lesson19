package ru.otus.lesson19.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.lesson19.converter.GenreConverter;
import ru.otus.lesson19.dao.GenreDao;
import ru.otus.lesson19.dto.GenreDto;
import ru.otus.lesson19.exception.GenreNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreServiceImpl implements GenreService{

    private final GenreDao genreDao;
    private final GenreConverter genreConverter;

    public List<GenreDto> getAllGenre() {
        return genreDao.findAll().stream().map(genreConverter::entityToDto).collect(Collectors.toList());
    }

    @Override
    public GenreDto getGenreById(Long id) throws GenreNotFoundException {
            return genreConverter.entityToDto(genreDao.findById(id).orElseThrow(() -> new GenreNotFoundException(id)));
    }
}
