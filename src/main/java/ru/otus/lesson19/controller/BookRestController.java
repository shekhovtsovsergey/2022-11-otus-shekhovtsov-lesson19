package ru.otus.lesson19.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.lesson19.converter.BookConverter;
import ru.otus.lesson19.dto.AuthorDto;
import ru.otus.lesson19.dto.BookDto;
import ru.otus.lesson19.dto.CommentDto;
import ru.otus.lesson19.dto.GenreDto;
import ru.otus.lesson19.service.LibraryService;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BookRestController {
    private final LibraryService libraryService;
    private final BookConverter bookConverter;


    @GetMapping("/authore")
    public List<AuthorDto> getAuthoreList() {
        return libraryService.getAllAuthore();
    }

    @GetMapping("/genre")
    public List<GenreDto> getGenreList() {
        return libraryService.getAllGenre();
    }

    @GetMapping("/book")
    public List<BookDto> getBookList() {
        return libraryService.getAllBooks();
    }

    @GetMapping("/book/{id}/info")
    public BookDto getBookInfoById(@PathVariable(name = "id") Long id) {
        return libraryService.getBookById(id);
    }

    @PostMapping("/book")
    public String createBook(String name, Long authorId, Long genreId) {
        libraryService.createBook(name, authorId, genreId);
        return "redirect:/book/";
    }


    @GetMapping("/book/{id}")
    public BookDto getBookById(@PathVariable(name = "id") Long id) {
        return libraryService.getBookById(id);
    }

    @DeleteMapping("/book/{id}")
    public List<BookDto> deleteBookById(@PathVariable(name = "id") Long id) {
        libraryService.deleteBookById(id);
        return libraryService.getAllBooks();
    }

    @GetMapping("/book/{id}/comment")
    public List<CommentDto> getCommentList(@PathVariable(name = "id") Long id) {
        return libraryService.getAllCommentsByBook(id);
    }

    @PutMapping("/book/{id}")
    public BookDto updateBook(@RequestBody BookDto bookDto) {
        return libraryService.updateBook(bookDto.getId(),bookDto.getName(),libraryService.getAuthorById(bookDto.getAuthor()).getId(),libraryService.getGenreById(bookDto.getGenre()).getId());
    }
}
