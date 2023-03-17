package ru.otus.lesson27.dao;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.lesson27.model.Book;

import java.util.Optional;

public interface BookDao extends JpaRepository<Book, Long> {

    @EntityGraph(attributePaths = "comments")
    Optional<Book> findById(Long id);

}