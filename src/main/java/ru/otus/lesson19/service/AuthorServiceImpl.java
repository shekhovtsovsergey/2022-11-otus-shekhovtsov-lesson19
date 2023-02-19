package ru.otus.lesson19.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.otus.lesson19.converter.AuthorConverter;
import ru.otus.lesson19.dao.AuthorDao;
import ru.otus.lesson19.dto.AuthorDto;
import ru.otus.lesson19.exception.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorServiceImpl implements AuthorService{

    private final AuthorDao authorDao;
    private final AuthorConverter authorConverter;

    @Override
    public List<AuthorDto> getAllAuthore() {
        return authorDao.findAll().stream().map(authorConverter::entityToDto).collect(Collectors.toList());
    }

    @Override
    public Object getAuthorById(Long id) {
        try {
            return authorConverter.entityToDto(authorDao.findById(id).orElseThrow(NotFoundException::new));
        } catch (NotFoundException e) {
            log.error("Author not found id={}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


}
