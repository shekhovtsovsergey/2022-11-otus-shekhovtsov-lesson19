package ru.otus.lesson19.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.lesson19.model.Genre;

public interface GenreDao extends JpaRepository<Genre, Long> {

}