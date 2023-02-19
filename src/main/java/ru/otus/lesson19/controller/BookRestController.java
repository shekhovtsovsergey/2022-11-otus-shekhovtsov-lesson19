package ru.otus.lesson19.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.lesson19.dto.AuthorDto;
import ru.otus.lesson19.dto.BookDto;
import ru.otus.lesson19.dto.CommentDto;
import ru.otus.lesson19.dto.GenreDto;
import ru.otus.lesson19.service.*;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class BookRestController {

    private final AuthorService authorService;
    private final GenreService genreService;
    private final CommentService commentService;
    private final BookService bookService;


    @GetMapping("/api/v1/author")
    public List<AuthorDto> getAuthoreList() {
        return authorService.getAllAuthore();
    }

    @GetMapping("/api/v1/genre")
    public List<GenreDto> getGenreList() {
        return genreService.getAllGenre();
    }

    @GetMapping("/api/v1/book")
    public List<BookDto> getBookList() {
        return bookService.getAllBooks();
    }

    @GetMapping("/api/v1/book/{id}")
    public Object getBookById(@PathVariable(name = "id") Long id) {
        return bookService.getBookById(id);
    }

    @DeleteMapping("/api/v1/book/{id}")
    public List<BookDto> deleteBookById(@PathVariable(name = "id") Long id) {
        bookService.deleteBookById(id);
        return bookService.getAllBooks();
    }

    @GetMapping("/api/v1/book/{id}/comment")
    public List<CommentDto> getCommentList(@PathVariable(name = "id") Long id) {
        return commentService.getAllCommentsByBook(id);
    }

    @PutMapping("/api/v1/book/{id}")
    public BookDto updateBook(@RequestBody BookDto bookDto) {
        return bookService.updateBook(bookDto);
    }

    @PostMapping("/api/v1/book")
    public BookDto createBook(@RequestBody BookDto bookDto) {
        return bookService.createBook(bookDto);
    }


}
