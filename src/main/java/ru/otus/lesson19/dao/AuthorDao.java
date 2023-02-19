package ru.otus.lesson19.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.lesson19.model.Author;

public interface AuthorDao extends JpaRepository<Author, Long> {


}
