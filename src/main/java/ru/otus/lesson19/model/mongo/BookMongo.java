package ru.otus.lesson19.model.mongo;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.List;
import java.util.Objects;

@Document(collection = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookMongo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private ObjectId id;
    private String name;
    @DBRef
    private AuthorMongo author;
    @DBRef
    private GenreMongo genre;
    @DBRef
    private List<CommentMongo> comment;


    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + name + '\'' +
                ", author=" + author +
                ", genre=" + genre +
                ", comments=" + comment +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass() && getClass() != o.getClass().getSuperclass()) return false;
        BookMongo book = (BookMongo) o;
        return id.equals(book.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,name,author,genre);
    }

}
