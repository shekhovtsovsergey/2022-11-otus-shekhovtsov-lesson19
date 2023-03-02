package ru.otus.lesson19.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.lesson19.dao.BookDao;
import ru.otus.lesson19.dto.BookDto;
import ru.otus.lesson19.exception.BookNotFoundException;
import ru.otus.lesson19.service.AuthorService;
import ru.otus.lesson19.service.BookService;
import ru.otus.lesson19.service.CommentService;
import ru.otus.lesson19.service.GenreService;

import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest
@DisplayName("Контроллер библиотеки должен")
public class BookRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private GenreService genreService;
    @MockBean
    private CommentService commentService;


    @Test
    @DisplayName("должен уметь создавать книгу")
    public void createBook_ReturnBook() throws Exception {

        String bookInJson = "{\"id\": 1, \"name\": \"Ned ad\",\"author\": 1,\"genre\": 1}";

        BookDto expectedBook = BookDto.builder().id(1L).name("Ned ad").author(1L).genre(1L).build();
        //given(bookService.createBook(bookInJson)).willReturn(bookInJson);
       // given(bookDao.save(expectedBook)).willReturn(expectedBook);
        when(bookService.createBook(expectedBook)).thenReturn(expectedBook);

        mockMvc.perform(post("/api/v1/book")
                        .content(bookInJson)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Book1"));


        verify(bookService).createBook(expectedBook);
    }


    @Test
    public void updateBook_ReturnBook() throws Exception {
        BookDto expectedBook = BookDto.builder().id(1L).name("Book1").build();
        given(bookService.updateBook(expectedBook)).willReturn(expectedBook);
        mockMvc.perform(put("/api/v1/book/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(expectedBook)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Book1"));
        verify(bookService).updateBook(expectedBook);
    }


    @Test
    public void getBookList_ReturnBookList() throws Exception {
        List<BookDto> expectedBookList = Arrays.asList(
                BookDto.builder().id(1L).name("Book1").build(),
                BookDto.builder().id(2L).name("Book2").build(),
                BookDto.builder().id(3L).name("Book3").build()
        );

        given(bookService.getAllBooks()).willReturn(expectedBookList);
        mockMvc.perform(get("/api/v1/book"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].name", Matchers.is("Book1")))
                .andExpect(jsonPath("$[1].id", Matchers.is(2)))
                .andExpect(jsonPath("$[1].name", Matchers.is("Book2")))
                .andExpect(jsonPath("$[2].id", Matchers.is(3)))
                .andExpect(jsonPath("$[2].name", Matchers.is("Book3")));
        verify(bookService).getAllBooks();
    }


    @Test
    public void getBookById_ReturnBook() throws Exception {
        BookDto expectedBook = BookDto.builder().id(1L).name("Book1").build();
        given(bookService.getBookById(1L)).willReturn(expectedBook);

        mockMvc.perform(get("/api/v1/book/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Book1"));

        verify(bookService).getBookById(1L);
    }


    @Test
    public void deleteBookById_ReturnVoid() throws Exception {
        mockMvc.perform(delete("/api/v1/book/1"))
                .andExpect(status().isOk());
        verify(bookService).deleteBookById(1L);
    }



    @Test
    public void handleNotFound_ReturnBadRequest() throws Exception {
        given(bookService.getBookById(1L)).willThrow(new BookNotFoundException("Book Not Found"));
        mockMvc.perform(get("/api/v1/book/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Book Not Found"));
        verify(bookService).getBookById(1L);
    }



    private String toJsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}