package ru.otus.lesson19.controller;

import lombok.RequiredArgsConstructor;;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.lesson19.service.LibraryService;

@Controller
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final LibraryService libraryService;

    @GetMapping("/all")
    public String getBookList(Model model) {
        model.addAttribute("books", libraryService.getAllBooks());
        return "book-list";
    }

    @GetMapping
    public String showForm(Model model, @RequestParam(name = "id", required = false) Long id) {
        model.addAttribute("authors", libraryService.getAllAuthore());
        model.addAttribute("genres", libraryService.getAllGenre());
        model.addAttribute("book", libraryService.getBookById(id));
        return "book-form";
    }

    @GetMapping("/delete")
    public String deleteById(@RequestParam(name = "id") Long id) {
        libraryService.deleteBookById(id);
        return "redirect:/book/all";
    }

    @GetMapping("comment/{bookId}")
    public String getCommentList(Model model, @PathVariable(name = "bookId") Long id) {
        model.addAttribute("comments", libraryService.getAllCommentsByBook(id));
        return "comment-list";
    }

    @PostMapping
    public String saveBook(Long id, String name, Long authorId, Long genreId) {
        libraryService.updateBook(id,name, authorId, genreId);
        return "redirect:/book/all";
    }

    @GetMapping("/{bookId}")
    public String info(Model model, @PathVariable(name = "bookId") Long id) {
        model.addAttribute("book", libraryService.getBookById(id));
        return "book-info";
    }
}
