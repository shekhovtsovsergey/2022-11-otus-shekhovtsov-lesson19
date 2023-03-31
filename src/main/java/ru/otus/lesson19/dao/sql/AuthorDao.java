package ru.otus.lesson19.dao.sql;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.lesson19.model.sql.Author;

public interface AuthorDao extends JpaRepository<Author, Long> {

}
