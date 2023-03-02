package ru.otus.lesson19.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.lesson19.dto.BookDto;
import ru.otus.lesson19.exception.BookNotFoundException;
import ru.otus.lesson19.service.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BookRestController {

    private final BookService bookService;

    @GetMapping("/api/v1/book")
    public List<BookDto> getBookList() {
        return bookService.getAllBooks();
    }

    @GetMapping("/api/v1/book/{id}")
    public BookDto getBookById(@PathVariable(name = "id") Long id) throws BookNotFoundException {
        return bookService.getBookById(id);
    }

    @DeleteMapping("/api/v1/book/{id}")
    public void deleteBookById(@PathVariable(name = "id") Long id) {
        bookService.deleteBookById(id);
    }

    @PutMapping("/api/v1/book/{id}")
    public BookDto updateBook(@RequestBody BookDto bookDto) {
        return bookService.updateBook(bookDto);
    }

    @PostMapping("/api/v1/book")
    public BookDto createBook(@RequestBody BookDto bookDto) {
        return bookService.createBook(bookDto);
    }

    @ExceptionHandler({BookNotFoundException.class})
    private ResponseEntity<String> handleNotFound(Exception e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
