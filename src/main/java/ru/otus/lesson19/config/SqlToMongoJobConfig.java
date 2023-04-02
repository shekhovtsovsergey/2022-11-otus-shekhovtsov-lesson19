package ru.otus.lesson19.config;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.hibernate.SessionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.database.HibernatePagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import ru.otus.lesson19.dao.mongo.AuthorRepository;
import ru.otus.lesson19.dao.mongo.BookRepository;
import ru.otus.lesson19.dao.mongo.GenreRepository;
import ru.otus.lesson19.exeption.EntityNotFoundException;
import ru.otus.lesson19.model.mongo.AuthorMongo;
import ru.otus.lesson19.model.mongo.BookMongo;
import ru.otus.lesson19.model.mongo.CommentMongo;
import ru.otus.lesson19.model.mongo.GenreMongo;
import ru.otus.lesson19.model.sql.Book;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;

@Configuration
@RequiredArgsConstructor
public class SqlToMongoJobConfig {

    private static final int CHUNK_SIZE = 5;
    public static final String JOB_NAME = "loadDataToMongo";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final EntityManagerFactory entityManagerFactory;
    private final MongoOperations mongoOperations;

    @Bean
    public Job loadDataToMongo() {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(loadBooksToMongo())
                .build();
    }

    //================================================
    // This block of code is responsible for the books
    //================================================

    @Bean
    public Step loadBooksToMongo() {
        return stepBuilderFactory.get("loadBooksToMongo")
                .<Book, BookMongo>chunk(CHUNK_SIZE)
                .reader(bookRdbReader())
                .processor(bookProcessor())
                .writer(bookMongoWriter())
                .build();
    }

    @Bean
    public HibernatePagingItemReader<Book> bookRdbReader() {
        var reader = new HibernatePagingItemReader<Book>();
        reader.setSessionFactory(entityManagerFactory.unwrap(SessionFactory.class));
        reader.setPageSize(CHUNK_SIZE);
        reader.setQueryString(
                "select book " +
                        "from Book book " +
                        "left join fetch book.author " +
                        "left join fetch book.genre " +
                        "left join fetch book.comments"
        );
        return reader;
    }

    @Bean
    public ItemProcessor<Book, BookMongo> bookProcessor() {
        return book -> {
            var authorName = book.getAuthor().getName();
            var author = authorRepository.findFirstByName(authorName);
            if (author == null) {
                throw new javax.persistence.EntityNotFoundException("Author not found");
            }
            var genreName = book.getGenre().getName();
            var genre = genreRepository.findFirstByName(genreName);
            if (genre == null) {
                throw new javax.persistence.EntityNotFoundException("Genre not found");
            }
            return new BookMongo(ObjectId.get(), book.getName(), author, genre, new ArrayList<CommentMongo>());
        };
    }

/*    @Bean
    public ItemProcessor<Book, BookMongo> bookProcessor() {
        return book -> {
            var author = new AuthorMongo(ObjectId.get(), book.getAuthor().getName());
            var genre = new GenreMongo(ObjectId.get(), book.getGenre().getName());
            return new BookMongo(ObjectId.get(), book.getName(), author, genre, new ArrayList<CommentMongo>());
        };
    }*/

    @Bean
    public MongoItemWriter<BookMongo> bookMongoWriter() {
        var writer = new MongoItemWriter<BookMongo>();
        writer.setCollection("ext_books");
        writer.setTemplate(mongoOperations);
        return writer;
    }
}
