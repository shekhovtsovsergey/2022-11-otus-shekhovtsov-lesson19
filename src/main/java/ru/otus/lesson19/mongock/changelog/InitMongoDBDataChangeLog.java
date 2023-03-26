package ru.otus.lesson19.mongock.changelog;


import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import com.mongodb.client.MongoDatabase;
import ru.otus.lesson19.mongo.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {

    private Author leanNielsen;
    private Author klausRifbjerg;
    private Author thorkildBjornvig;
    private Author cecilBodker;
    private Author greteStenbaek;

    private Genre documental;
    private Genre historycal;

    private Book bookOne;
    private Book bookTwo;
    private Book bookThree;
    private Book bookFour;
    private Book bookFive;


    @ChangeSet(order = "000", id = "dropDb", author = "shekhovtsov", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "001", id = "initAuthors", author = "shekhovtsov", runAlways = true)
    public void initAuthors(AuthorRepository repository) {
        leanNielsen = repository.save(new Author(null, "Lean Nielsen"));
        klausRifbjerg = repository.save(new Author(null, "Klaus Rifbjerg"));
        thorkildBjornvig = repository.save(new Author(null, "Thorkild Bjørnvig"));
        cecilBodker = repository.save(new Author(null, "Cecil Bødker"));
        greteStenbaek = repository.save(new Author(null, "Grete Stenbæk"));
    }

    @ChangeSet(order = "002", id = "initGenres", author = "shekhovtsov", runAlways = true)
    public void initGenres(GenreRepository repository) {
        documental = repository.save(new Genre(null, "Documental"));
        historycal = repository.save(new Genre(null, "History"));
    }

    @ChangeSet(order = "003", id = "initBooks", author = "shekhovtsov", runAlways = true)
    public void initBooks(BookRepository bookRepository, CommentRepository commentRepository) {
        bookOne = bookRepository.save(new Book(null, "Ned ad trappen, ud på gaden (Danish Edition)", leanNielsen, documental,new ArrayList<Comment>()));
        bookTwo = bookRepository.save(new Book(null, "Kesses krig (Unge læsere) (Danish Edition)", leanNielsen, documental,new ArrayList<Comment>()));
        bookThree = bookRepository.save(new Book(null, "Hjørnestuen og månehavet: Erindringer 1934-1938 (Danish Edition)", leanNielsen, documental,new ArrayList<Comment>()));
        bookFour = bookRepository.save(new Book(null, "Vandgården: Roman (Danish Edition)", leanNielsen, documental,new ArrayList<Comment>()));
        bookFive = bookRepository.save(new Book(null, "Thea (Danish Edition)", leanNielsen, documental,new ArrayList<Comment>()));
    }

    @ChangeSet(order = "004", id = "initComments", author = "shekhovtsov", runAlways = true)
    public void initComments(MongockTemplate mongoTemplate) {
        List<Comment> comments = IntStream.range(0, 5)
                .mapToObj(i -> new Comment(null, "shekhovtsov", "habe nichts verstanden", bookOne))
                .collect(Collectors.toList());
        mongoTemplate.insertAll(comments);
        bookOne.getComment().addAll(comments);
        mongoTemplate.save(bookOne);
    }
}
