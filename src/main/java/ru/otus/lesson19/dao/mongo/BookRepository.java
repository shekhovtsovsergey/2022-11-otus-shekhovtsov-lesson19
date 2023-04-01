package ru.otus.lesson19.dao.mongo;


import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.lesson19.model.mongo.BookMongo;

public interface BookRepository extends MongoRepository<BookMongo, String> {
}
