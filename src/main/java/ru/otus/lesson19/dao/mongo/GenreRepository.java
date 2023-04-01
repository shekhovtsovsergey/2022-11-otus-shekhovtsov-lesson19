package ru.otus.lesson19.dao.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.lesson19.model.mongo.GenreMongo;

public interface GenreRepository extends MongoRepository<GenreMongo, String> {

}
