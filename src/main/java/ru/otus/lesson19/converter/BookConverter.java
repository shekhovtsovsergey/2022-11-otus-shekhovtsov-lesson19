package ru.otus.lesson19.converter;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.lesson19.dto.BookDto;
import ru.otus.lesson19.model.Book;

@Component
@RequiredArgsConstructor
public class BookConverter {

    public BookDto entityToDto(Book book) {
        return new BookDto(book.getId(), book.getName(),book.getAuthor().getName(),book.getGenre().getName());
    }

}
