package ru.otus.lesson19.dao.sql;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.lesson19.model.sql.Author;
import ru.otus.lesson19.model.sql.Genre;

import java.util.Optional;

public interface GenreDao extends JpaRepository<Genre, Long> {

    Genre findFirstByName(String name);
}