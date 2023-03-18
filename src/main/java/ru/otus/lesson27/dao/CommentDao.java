package ru.otus.lesson27.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.lesson27.model.Book;
import ru.otus.lesson27.model.Comment;

public interface CommentDao extends JpaRepository<Comment, Long> {

    void deleteByBook(Book book);
}
