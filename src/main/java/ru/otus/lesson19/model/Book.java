package ru.otus.lesson19.model;


import lombok.*;
import org.hibernate.annotations.BatchSize;
import ru.otus.lesson19.dto.BookDto;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(name = "Book.allAttributes", includeAllAttributes = true)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @ManyToOne
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;


    @OneToMany()
    @BatchSize(size = 20)
    @JoinColumn(name = "book_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_book_comment"),
            updatable = false, insertable = false)
    private List<Comment> comments;


}
