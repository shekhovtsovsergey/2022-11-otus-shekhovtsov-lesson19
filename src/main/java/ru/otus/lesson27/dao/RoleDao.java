package ru.otus.lesson27.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.lesson27.model.Role;

public interface RoleDao extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
