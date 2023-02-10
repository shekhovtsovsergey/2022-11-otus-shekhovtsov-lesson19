package ru.otus.lesson19.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.lesson19.dto.BookDto;
import ru.otus.lesson19.dto.CommentDto;
import ru.otus.lesson19.service.LibraryService;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BookRestController {
    private final LibraryService libraryService;

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
        return "redirect:/book/all";
    }

    @PutMapping("/book/{id}")
    public String updateBook(Long id, String name, Long authorId, Long genreId) {
        libraryService.updateBook(id,name, authorId, genreId);
        return "redirect:/book/all";
    }

    @GetMapping("/book/{id}")
    public BookDto getBookById(@RequestParam(name = "id") Long id) {
        return libraryService.getBookById(id);
    }

    @DeleteMapping("/book/{id}")
    public String deleteBookById(@RequestParam(name = "id") Long id) {
        libraryService.deleteBookById(id);
        return "redirect:/book/all";
    }

    @GetMapping("/book/{id}/comment")
    public List<CommentDto> getCommentList(@PathVariable(name = "id") Long id) {
        return libraryService.getAllCommentsByBook(id);
    }
}
