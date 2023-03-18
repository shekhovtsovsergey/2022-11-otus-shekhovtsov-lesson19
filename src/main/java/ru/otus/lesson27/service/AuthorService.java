package ru.otus.lesson27.service;

import ru.otus.lesson27.dto.AuthorDto;
import ru.otus.lesson27.exception.AuthorNotFoundException;
import java.util.List;

public interface AuthorService {

    List<AuthorDto> getAllAuthore() throws AuthorNotFoundException;

}
