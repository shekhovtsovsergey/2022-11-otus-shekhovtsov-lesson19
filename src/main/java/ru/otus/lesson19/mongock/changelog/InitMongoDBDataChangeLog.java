package ru.otus.lesson19.mongock.changelog;


import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import com.mongodb.client.MongoDatabase;
import ru.otus.lesson19.dao.mongo.AuthorRepository;
import ru.otus.lesson19.dao.mongo.BookRepository;
import ru.otus.lesson19.dao.mongo.CommentRepository;
import ru.otus.lesson19.dao.mongo.GenreRepository;
import ru.otus.lesson19.model.mongo.AuthorMongo;
import ru.otus.lesson19.model.mongo.BookMongo;
import ru.otus.lesson19.model.mongo.CommentMongo;
import ru.otus.lesson19.model.mongo.GenreMongo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {

    private AuthorMongo leanNielsen;
    private AuthorMongo klausRifbjerg;
    private AuthorMongo thorkildBjornvig;
    private AuthorMongo cecilBodker;
    private AuthorMongo greteStenbaek;

    private GenreMongo documental;
    private GenreMongo historycal;

    private BookMongo bookOne;
    private BookMongo bookTwo;
    private BookMongo bookThree;
    private BookMongo bookFour;
    private BookMongo bookFive;


    @ChangeSet(order = "000", id = "dropDb", author = "shekhovtsov", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "001", id = "initAuthors", author = "shekhovtsov", runAlways = true)
    public void initAuthors(AuthorRepository repository) {
        leanNielsen = repository.save(new AuthorMongo(null, "Lean Nielsen"));
        klausRifbjerg = repository.save(new AuthorMongo(null, "Klaus Rifbjerg"));
        thorkildBjornvig = repository.save(new AuthorMongo(null, "Thorkild Bjørnvig"));
        cecilBodker = repository.save(new AuthorMongo(null, "Cecil Bødker"));
        greteStenbaek = repository.save(new AuthorMongo(null, "Grete Stenbæk"));
    }

    @ChangeSet(order = "002", id = "initGenres", author = "shekhovtsov", runAlways = true)
    public void initGenres(GenreRepository repository) {
        documental = repository.save(new GenreMongo(null, "Documental"));
        historycal = repository.save(new GenreMongo(null, "History"));
    }

/*
    @ChangeSet(order = "003", id = "initBooks", author = "shekhovtsov", runAlways = true)
    public void initBooks(BookRepository bookRepository, CommentRepository commentRepository) {
        bookOne = bookRepository.save(new BookMongo(null, "Ned ad trappen, ud på gaden (Danish Edition)", leanNielsen, documental,new ArrayList<CommentMongo>()));
        bookTwo = bookRepository.save(new BookMongo(null, "Kesses krig (Unge læsere) (Danish Edition)", leanNielsen, documental,new ArrayList<CommentMongo>()));
        bookThree = bookRepository.save(new BookMongo(null, "Hjørnestuen og månehavet: Erindringer 1934-1938 (Danish Edition)", leanNielsen, documental,new ArrayList<CommentMongo>()));
        bookFour = bookRepository.save(new BookMongo(null, "Vandgården: Roman (Danish Edition)", leanNielsen, documental,new ArrayList<CommentMongo>()));
        bookFive = bookRepository.save(new BookMongo(null, "Thea (Danish Edition)", leanNielsen, documental,new ArrayList<CommentMongo>()));
    }
*/

    @ChangeSet(order = "003", id = "initBooks", author = "shekhovtsov", runAlways = true)
    public void initBooks(BookRepository bookRepository, CommentRepository commentRepository) {
        for (int i = 1; i <= 50000; i++) {
            BookMongo book = new BookMongo(null, "Book " + i, leanNielsen, documental, new ArrayList<CommentMongo>());
            bookRepository.save(book);
        }
    }


/*    @ChangeSet(order = "004", id = "initComments", author = "shekhovtsov", runAlways = true)
    public void initComments(MongockTemplate mongoTemplate) {
        List<CommentMongo> comments = IntStream.range(0, 5)
                .mapToObj(i -> new CommentMongo(null, "shekhovtsov", "habe nichts verstanden", bookOne))
                .collect(Collectors.toList());
        mongoTemplate.insertAll(comments);
        bookOne.getComment().addAll(comments);
        mongoTemplate.save(bookOne);
    }*/
}
