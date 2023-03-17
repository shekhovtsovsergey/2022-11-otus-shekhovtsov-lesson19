package ru.otus.lesson27.controller;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.lesson27.dto.GenreDto;
import ru.otus.lesson27.exception.GenreNotFoundException;
import ru.otus.lesson27.service.*;
import java.util.Arrays;
import java.util.List;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest
@DisplayName("Контроллер библиотеки")
public class GenreRestControllerTest {

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
    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(username = "user")
    @DisplayName("должен уметь возвращать список жанров")
    void getGenreList_ShouldReturnAllGenres() throws Exception {
        List<GenreDto> genres = Arrays.asList(
                new GenreDto(1L, "pop"),
                new GenreDto(2L, "rock"));
        given(genreService.getAllGenre()).willReturn(genres);
        mockMvc.perform(get("/api/v1/genre"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("pop")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("rock")));
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("должен уметь ловить ошибки и возвращать бэд-реквест")
    void getGenreList_WhenGenresNotFound_ShouldReturnBadRequest() throws Exception {
        given(genreService.getAllGenre()).willThrow(new GenreNotFoundException("Genres not found, check your request"));
        mockMvc.perform(get("/api/v1/genre"))
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("должен перенаправлять на страницу аутентификации для доступа к списку жанров")
    void whenGetGenreList_thenRedirectToAuthenticationPage() throws Exception {
        mockMvc.perform(get("/api/v1/genre"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
}