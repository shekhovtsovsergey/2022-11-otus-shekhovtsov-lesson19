package ru.otus.lesson19.dao.mongo;


import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.lesson19.model.mongo.Comment;

public interface CommentRepository extends MongoRepository<Comment, String> {
}
