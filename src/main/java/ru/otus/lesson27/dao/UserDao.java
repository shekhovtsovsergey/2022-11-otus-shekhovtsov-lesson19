package ru.otus.lesson27.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.lesson27.model.User;
import java.util.List;

public interface UserDao extends JpaRepository<User, Long> {

    User findByUsername(String name);

}

