package ru.otus.lesson19.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.lesson19.dto.AuthorDto;
import ru.otus.lesson19.dto.BookDto;
import ru.otus.lesson19.dto.CommentDto;
import ru.otus.lesson19.dto.GenreDto;
import ru.otus.lesson19.service.LibraryService;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;

@SpringBootTest
@DisplayName("Контроллер библиотеки должен")
public class BookRestControllerTest {

        @Mock
        private LibraryService libraryService;
        @InjectMocks
        private BookRestController bookRestController;


        @Test
        @DisplayName("должен уметь возвращать лист авторов")
        public void getAuthoreList_ShouldReturnAuthoreList_WhenCalled(){
            List<AuthorDto> authorDtos = new ArrayList<>();
            Mockito.when(libraryService.getAllAuthore()).thenReturn(authorDtos);
            List<AuthorDto> result = bookRestController.getAuthoreList();
            Assert.assertEquals(result, authorDtos);
        }

        @Test
        @DisplayName("должен уметь возвращать лист жанров")
        public void getGenreList_ShouldReturnGenreList_WhenCalled(){
            List<GenreDto> genreDtos = new ArrayList<>();
            Mockito.when(libraryService.getAllGenre()).thenReturn(genreDtos);
            List<GenreDto> result = bookRestController.getGenreList();
            Assert.assertEquals(result, genreDtos);
        }

        @Test
        @DisplayName("должен уметь возвращать лист книг")
        public void getBookList_ShouldReturnBookList_WhenCalled(){
            List<BookDto> bookDtos = new ArrayList<>();
            Mockito.when(libraryService.getAllBooks()).thenReturn(bookDtos);
            List<BookDto> result = bookRestController.getBookList();
            Assert.assertEquals(result, bookDtos);
        }

        @Test
        @DisplayName("должен уметь возвращать информацию о книге")
        public void getBookInfoById_ShouldReturnBookInfoById_WhenCalled(){
            Long id = 1L;
            BookDto bookDto = new BookDto();
            Mockito.when(libraryService.getBookById(id)).thenReturn(bookDto);
            BookDto result = bookRestController.getBookInfoById(id);
            Assert.assertEquals(result, bookDto);
        }

        @Test
        @DisplayName("должен уметь удалять книгу по id")
        public void deleteBookById_ShouldDeleteBookById_WhenCalled(){
            Long id = 1L;
            List<BookDto> bookDtos = new ArrayList<>();
            Mockito.when(libraryService.deleteBookById(id)).thenReturn(bookDtos);
            List<BookDto> result = bookRestController.deleteBookById(id);
            Assert.assertEquals(result, bookDtos);
        }



        @Test
        @DisplayName("должен уметь возвращать лист комментариев")
        public void getCommentList_ShouldReturnCommentList_WhenCalled(){
            Long id = 1L;
            List<CommentDto> commentDtos = new ArrayList<>();
            Mockito.when(libraryService.getAllCommentsByBook(id)).thenReturn(commentDtos);
            List<CommentDto> result = bookRestController.getCommentList(id);
            Assert.assertEquals(result, commentDtos);
        }

        @Test
        @DisplayName("должен уметь обновлять книгу")
        public void updateBook_ShouldUpdateBook_WhenCalled(){
            BookDto bookDto = new BookDto();
            Mockito.when(libraryService.updateBook(bookDto)).thenReturn(bookDto);
            BookDto result = bookRestController.updateBook(bookDto);
            Assert.assertEquals(result, bookDto);
        }

        @Test
        @DisplayName("должен уметь создавать книгу")
        public void createBook_ShouldCreateBook_WhenCalled() {
            BookDto bookDto = new BookDto();
            Mockito.when(libraryService.createBook(bookDto)).thenReturn(bookDto);
            BookDto result = bookRestController.createBook(bookDto);
            Assert.assertEquals(result, bookDto);
        }

}
