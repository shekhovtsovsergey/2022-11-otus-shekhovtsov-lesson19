package ru.otus.lesson19.shell;


import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.lesson19.dao.AuthorDao;
import ru.otus.lesson19.dao.BookDao;
import ru.otus.lesson19.model.Author;
import ru.otus.lesson19.mongo.Book;
import ru.otus.lesson19.mongo.BookRepository;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class LibraryCommands {

    private final AuthorDao authorDao;
    private final BookRepository bookRepository;

    @ShellMethod(value = "Get", key = {"m"})
    public String authors() {
        List<Book> bookDaoList = bookRepository.findAll();
        return String.format("Все книги: %s", bookDaoList);
    }

    @ShellMethod(value = "Get", key = {"s"})
    public String author() {
        List<Author> authors = authorDao.findAll();
        return String.format("Все книги: %s", authors);
    }

}
