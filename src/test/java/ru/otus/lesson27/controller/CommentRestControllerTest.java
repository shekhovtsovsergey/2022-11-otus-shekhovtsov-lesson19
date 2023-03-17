package ru.otus.lesson27.controller;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.lesson27.dto.CommentDto;
import ru.otus.lesson27.exception.BookNotFoundException;
import ru.otus.lesson27.service.*;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest
@DisplayName("Контроллер комментариева")
public class CommentRestControllerTest {

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
    @DisplayName("должен уметь возвращать список комментариев по книге")
    void getCommentList_shouldReturn200() throws Exception {
        List<CommentDto> commentDtos = Arrays.asList(
                new CommentDto(1L, "Eugene Onegin","Pushkin","Good Book"));
        given(commentService.getAllCommentsByBook(1L)).willReturn(commentDtos);
        mockMvc.perform(get("/api/v1/book/1/comment"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].book", is("Eugene Onegin")));
    }
    @Test
    @WithMockUser(username = "user")
    @DisplayName("должен уметь ловить ошибки и возвращать бэд-реквест")
    void getCommentList_shouldReturn400_whenNoBookFound() throws Exception {
        Long bookId = 1L;
        when(commentService.getAllCommentsByBook(bookId)).thenThrow(new BookNotFoundException("Book not found, check your request"));
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/v1/book/1/comment")
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn();
    }
    @Test
    @DisplayName("должен требовать авторизацию для доступа к ресурсу")
    void getCommentList_shouldReturn401_whenNotAuthorized() throws Exception {
        mockMvc.perform(get("/api/v1/book/1/comment"))
                .andExpect(redirectedUrl("http://localhost/login"));
    }
}