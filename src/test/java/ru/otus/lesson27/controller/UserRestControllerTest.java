package ru.otus.lesson27.controller;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.otus.lesson27.service.*;


@WebMvcTest
@DisplayName("Контроллер пользователей")
public class UserRestControllerTest {

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
    @DisplayName("должен уметь возвращать страницу для неавторизованных")
    public void testLoginPageAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("должен уметь возвращать страницу регистрации для неавторизованных")
    public void testRegistrationPageAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login/register"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    @DisplayName("должен уметь сохранять пользователей")
    public void testUserRegistration() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/login/register/save")
                        .with(SecurityMockMvcRequestPostProcessors.user("username").password("password"))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "testuser")
                        .param("email", "testuser@test.com")
                        .param("firstName", "Test")
                        .param("lastName", "User"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}