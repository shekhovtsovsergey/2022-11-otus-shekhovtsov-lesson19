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
import java.util.ArrayList;

@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {

    private AuthorMongo leanNielsen;
    private AuthorMongo klausRifbjerg;
    private AuthorMongo thorkildBjornvig;
    private AuthorMongo cecilBodker;
    private AuthorMongo greteStenbaek;

    private GenreMongo documental;
    private GenreMongo historycal;


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

    @ChangeSet(order = "003", id = "initBooks", author = "shekhovtsov", runAlways = true)
    public void initBooks(BookRepository bookRepository, CommentRepository commentRepository) {
        for (int i = 1; i <= 100; i++) {
            BookMongo book = new BookMongo(null, "Book " + i, leanNielsen, documental, new ArrayList<CommentMongo>());
            bookRepository.save(book);
        }
    }
}
