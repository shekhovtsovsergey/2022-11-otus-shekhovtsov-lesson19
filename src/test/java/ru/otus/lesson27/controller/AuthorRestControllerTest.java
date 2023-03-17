package ru.otus.lesson27.controller;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.lesson27.dto.AuthorDto;
import ru.otus.lesson27.exception.AuthorNotFoundException;
import ru.otus.lesson27.service.AuthorService;
import ru.otus.lesson27.service.BookService;
import ru.otus.lesson27.service.CommentService;
import ru.otus.lesson27.service.GenreService;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest
@DisplayName("Контроллер библиотеки")
public class AuthorRestControllerTest {

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
    @DisplayName("должен уметь получать список авторов")
    void whenGetAuthoreList_thenReturnAuthoreList() throws Exception {

        List<AuthorDto> authorDtoList = Arrays.asList(new AuthorDto(), new AuthorDto());
        given(authorService.getAllAuthore()).willReturn(authorDtoList);

        mockMvc.perform(get("/api/v1/author"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(authorService).getAllAuthore();
    }


    @Test
    @DisplayName("должен уметь ловить ошибки и возвращать бэд-реквест")
    void getAuthorList_WhenAuthorNotFound_ShouldReturnBadRequest() throws Exception {
        given(authorService.getAllAuthore()).willThrow(new AuthorNotFoundException("Authors not found, check your request"));

        mockMvc.perform(get("/api/v1/author"))
                .andExpect(status().isBadRequest());
    }


}