package ru.otus.lesson27.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.lesson27.model.Genre;

public interface GenreDao extends JpaRepository<Genre, Long> {

}