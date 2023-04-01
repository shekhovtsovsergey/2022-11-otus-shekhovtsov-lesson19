package ru.otus.lesson19.config;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import ru.otus.lesson19.dao.sql.BookDao;
import ru.otus.lesson19.model.mongo.BookMongo;
import ru.otus.lesson19.model.sql.Author;
import ru.otus.lesson19.model.sql.Book;
import ru.otus.lesson19.model.sql.Comment;
import ru.otus.lesson19.model.sql.Genre;
import org.springframework.batch.core.Job;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.HashMap;

@Configuration
@RequiredArgsConstructor
public class MongoToSqlJobConfig {

    private static final int CHUNK_SIZE = 5;
    public static final String JOB_NAME = "loadDataToSql";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final BookDao bookDao;
    private final EntityManagerFactory entityManagerFactory;
    private final MongoOperations mongoOperations;

    @Bean
    public Job loadDataToSql() {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(loadBooksToSql())
                .build();
    }


    @Bean
    public Step loadBooksToSql() {
        return stepBuilderFactory.get("loadBooksToMongo")
                .<BookMongo, Book>chunk(CHUNK_SIZE)
                .reader(bookMongoReader())
                .processor(bookMongoProcessor())
                .writer(bookSqlWriter())
                .build();
    }


    @Bean
    public MongoItemReader<BookMongo> bookMongoReader() {
        var reader = new MongoItemReader<BookMongo>();
        reader.setTemplate(mongoOperations);
        reader.setCollection("books");
        reader.setQuery("{}");
        reader.setSort(new HashMap<String, Sort.Direction>() {{
            put("id", Sort.Direction.ASC);
        }});
        reader.setTargetType(BookMongo.class);
        return reader;
    }


    @Bean
    public ItemProcessor<BookMongo, Book> bookMongoProcessor() {
        return bookMongo -> {
            var author = new Author(null,bookMongo.getAuthor().getName());
            var genre = new Genre(null,bookMongo.getGenre().getName());
            return new Book(null,bookMongo.getName(), author, genre, new ArrayList<Comment>());
        };
    }

    @Bean
    public JpaItemWriter<Book> bookSqlWriter() {
        var writer = new JpaItemWriter<Book>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
}
