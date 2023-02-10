package ru.otus.lesson19.model;


import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

@Entity
@Table(name = "comments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(name = "Comments-bookId", attributeNodes = @NamedAttributeNode("book"))
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @Fetch(value = FetchMode.JOIN)
    @JoinColumn(name = "book_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_books_comments"),
            nullable = false)
    private Book book;

    @Column(name = "author_name", nullable = false)
    private String authorName;

    @Column(name = "comment", nullable = false)
    private String comment;


    public String toString() {
        return "Comment{"
                + "id='" + id + '\''
                + ", authorName=" + authorName
                + ", comment=" + comment
                + '}';
    }


}
