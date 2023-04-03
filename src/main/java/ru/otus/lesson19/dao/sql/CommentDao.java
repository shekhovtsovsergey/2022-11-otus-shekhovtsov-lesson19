package ru.otus.lesson19.dao.sql;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.lesson19.model.sql.Book;
import ru.otus.lesson19.model.sql.Comment;

public interface CommentDao extends JpaRepository<Comment, Long> {

    void deleteByBook(Book book);
}
