package ru.otus.lesson19.model.mongo;


import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Document(collection = "authors")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AuthorMongo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private ObjectId id;
    private String name;
}
