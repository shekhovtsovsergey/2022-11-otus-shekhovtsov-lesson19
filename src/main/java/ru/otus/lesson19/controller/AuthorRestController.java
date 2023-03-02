package ru.otus.lesson19.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.lesson19.dto.AuthorDto;
import ru.otus.lesson19.service.AuthorService;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthorRestController {

    private final AuthorService authorService;

    @GetMapping("/api/v1/author")
    public List<AuthorDto> getAuthoreList() {
        return authorService.getAllAuthore();
    }
}
