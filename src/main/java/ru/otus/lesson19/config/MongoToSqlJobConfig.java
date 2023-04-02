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
import ru.otus.lesson19.dao.sql.AuthorDao;
import ru.otus.lesson19.dao.sql.BookDao;
import ru.otus.lesson19.dao.sql.GenreDao;
import ru.otus.lesson19.model.mongo.AuthorMongo;
import ru.otus.lesson19.model.mongo.BookMongo;
import ru.otus.lesson19.model.mongo.GenreMongo;
import ru.otus.lesson19.model.sql.Author;
import ru.otus.lesson19.model.sql.Book;
import ru.otus.lesson19.model.sql.Comment;
import ru.otus.lesson19.model.sql.Genre;
import org.springframework.batch.core.Job;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
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
    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final EntityManagerFactory entityManagerFactory;
    private final MongoOperations mongoOperations;


    @Bean
    public Job loadDataToSql() {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(loadAuthorToSql())
                .next(loadGenreToSql())
                .next(loadBooksToSql())
                .build();
    }

    //==================================================
    // This block of code is responsible for the authors
    //==================================================

    @Bean
    public Step loadAuthorToSql() {
        return stepBuilderFactory.get("loadAuthorToSql")
                .<AuthorMongo, Author>chunk(CHUNK_SIZE)
                .reader(authorMongoReader())
                .processor(authorMongoProcessor())
                .writer(authorSqlWriter())
                .build();
    }


    @Bean
    public MongoItemReader<AuthorMongo> authorMongoReader() {
        var reader = new MongoItemReader<AuthorMongo>();
        reader.setTemplate(mongoOperations);
        reader.setCollection("authors");
        reader.setQuery("{}");
        reader.setSort(new HashMap<String, Sort.Direction>() {{
            put("id", Sort.Direction.ASC);
        }});
        reader.setTargetType(AuthorMongo.class);
        return reader;
    }

    @Bean
    public ItemProcessor<AuthorMongo, Author> authorMongoProcessor() {
        return authorMongo -> {
            return new Author(null,authorMongo.getName());
        };
    }

    @Bean
    public JpaItemWriter<Author> authorSqlWriter() {
        var writer = new JpaItemWriter<Author>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    //=================================================
    // This block of code is responsible for the genres
    //=================================================

    @Bean
    public Step loadGenreToSql() {
        return stepBuilderFactory.get("loadGenreToSql")
                .<GenreMongo, Genre>chunk(CHUNK_SIZE)
                .reader(genreMongoReader())
                .processor(genreMongoProcessor())
                .writer(genreSqlWriter())
                .build();
    }


    @Bean
    public MongoItemReader<GenreMongo> genreMongoReader() {
        var reader = new MongoItemReader<GenreMongo>();
        reader.setTemplate(mongoOperations);
        reader.setCollection("genres");
        reader.setQuery("{}");
        reader.setSort(new HashMap<String, Sort.Direction>() {{
            put("id", Sort.Direction.ASC);
        }});
        reader.setTargetType(GenreMongo.class);
        return reader;
    }

    @Bean
    public ItemProcessor<GenreMongo, Genre> genreMongoProcessor() {
        return genreMongo -> {
            return new Genre(null,genreMongo.getName());
        };
    }

    @Bean
    public JpaItemWriter<Genre> genreSqlWriter() {
        var writer = new JpaItemWriter<Genre>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }


    //================================================
    // This block of code is responsible for the books
    //================================================

    @Bean
    public Step loadBooksToSql() {
        return stepBuilderFactory.get("loadBooksToSql")
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
            Author author = authorDao.findFirstByName(bookMongo.getAuthor().getName());
            if (author == null) {
                throw new EntityNotFoundException("Author not found");
            }

            var genre = genreDao.findFirstByName(bookMongo.getGenre().getName());
            if (genre == null) {
                throw new EntityNotFoundException("Genre not found");
            }
            return new Book(null, bookMongo.getName(), author, genre, new ArrayList<Comment>());
        };
    }

    @Bean
    public JpaItemWriter<Book> bookSqlWriter() {
        var writer = new JpaItemWriter<Book>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
}
