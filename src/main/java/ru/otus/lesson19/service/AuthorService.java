package ru.otus.lesson19.service;

import ru.otus.lesson19.dto.AuthorDto;
import ru.otus.lesson19.exception.AuthorNotFoundException;
import java.util.List;

public interface AuthorService {

    List<AuthorDto> getAllAuthore() throws AuthorNotFoundException;

}
