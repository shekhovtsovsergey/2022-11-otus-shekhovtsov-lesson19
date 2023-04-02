package ru.otus.lesson19.dao.mongo;


import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.lesson19.model.mongo.AuthorMongo;
import ru.otus.lesson19.model.sql.Author;

public interface AuthorRepository extends MongoRepository<AuthorMongo, String> {
    AuthorMongo findFirstByName(String name);
}
