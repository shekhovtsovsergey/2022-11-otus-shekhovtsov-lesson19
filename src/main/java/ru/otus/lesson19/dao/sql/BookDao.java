package ru.otus.lesson19.dao.sql;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.lesson19.model.sql.Book;

public interface BookDao extends JpaRepository<Book, Long> {

}