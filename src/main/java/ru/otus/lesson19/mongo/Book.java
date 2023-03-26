package ru.otus.lesson19.mongo;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;

@Document(collection = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    private String id;
    private String name;
    @DBRef
    private Author author;
    @DBRef
    private Genre genre;
    @DBRef
    private List<Comment> comment;


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
        Book book = (Book) o;
        return id.equals(book.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,name,author,genre);
    }

}
