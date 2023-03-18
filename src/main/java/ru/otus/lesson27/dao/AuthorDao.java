package ru.otus.lesson27.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.lesson27.model.Author;

public interface AuthorDao extends JpaRepository<Author, Long> {

}
