package ru.otus.lesson19.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.lesson19.model.Book;
import ru.otus.lesson19.model.Comment;

public interface CommentDao extends JpaRepository<Comment, Long> {

    void deleteByBook(Book book);

}
