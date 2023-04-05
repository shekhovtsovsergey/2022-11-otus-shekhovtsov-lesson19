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
import org.springframework.transaction.annotation.Transactional;
import ru.otus.lesson19.dao.sql.AuthorDao;
import ru.otus.lesson19.dao.sql.BookDao;
import ru.otus.lesson19.dao.sql.GenreDao;
import ru.otus.lesson19.model.mongo.AuthorMongo;
import ru.otus.lesson19.model.mongo.BookMongo;
import ru.otus.lesson19.model.mongo.GenreMongo;
import ru.otus.lesson19.model.sql.Author;
import ru.otus.lesson19.model.sql.Book;
import ru.otus.lesson19.model.sql.Genre;
import org.springframework.batch.core.Job;
import ru.otus.lesson19.service.CacheService;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Configuration
@RequiredArgsConstructor
public class MongoToSqlJobConfig {

    private static final int CHUNK_SIZE = 100000;
    public static final String JOB_NAME = "loadDataToSql";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final EntityManagerFactory entityManagerFactory;
    private final MongoOperations mongoOperations;
    private final CacheService cacheService;


    @Bean
    public Job loadDataToSql() {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(loadAuthorToSql())
                .next(loadGenreToSql())
                .next(loadBooksToSql())
                .build();
    }


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


    @Bean
    public Step loadBooksToSql() {
        return stepBuilderFactory.get("loadBooksToSql")
                .<BookMongo, Book>chunk(CHUNK_SIZE)
                .reader(bookMongoReader(mongoOperations))
                .processor(bookMongoProcessor())
                .writer(bookSqlWriter())
                .build();
    }

    @Bean
    public MongoItemReader<BookMongo> bookMongoReader(MongoOperations mongoOperations) {
        var reader = new MongoItemReader<BookMongo>();
        reader.setTemplate(mongoOperations);
        reader.setCollection("books");
        reader.setQuery("{}");
        reader.setSort(new HashMap<String, Sort.Direction>() {{
            put("id", Sort.Direction.ASC);
        }});
        reader.setTargetType(BookMongo.class);
        reader.setPageSize(100000);
        return reader;
    }

    @Bean
    public ItemProcessor<BookMongo, Book> bookMongoProcessor() {
        return bookMongo -> {
            return new Book(
                    null,
                    bookMongo.getName(),
                    new Author(null, bookMongo.getAuthor().getName()),
                    new Genre(null, bookMongo.getGenre().getName()),
                    new ArrayList<>()
            );
        };
    }


    @Transactional
    @Bean
    public JpaItemWriter<Book> bookSqlWriter() {
        var writer = new JpaItemWriter<Book>();
        writer.setEntityManagerFactory(entityManagerFactory);
        JpaItemWriter<Book> cachedWriter = new JpaItemWriter<Book>() {

            @Override
            public void write(List<? extends Book> items) {
                List<Book> booksToSave = new ArrayList<>();
                for (Book item : items) {
                    Author author = cacheService.getAuthorByName(item.getAuthor().getName());
                    Genre genre = cacheService.getGenreByName(item.getGenre().getName());
                    if (author == null) {
                        author = authorDao.findFirstByName(item.getAuthor().getName());
                        if (author != null) {
                            cacheService.putAuthor(author);
                        }
                    }
                    if (genre == null) {
                        genre = genreDao.findFirstByName(item.getGenre().getName());
                        if (genre != null) {
                            cacheService.putGenre(genre);
                        }
                    }
                    item.setAuthor(author);
                    item.setGenre(genre);
                    booksToSave.add(item);
                }
                bookDao.saveAll(booksToSave);
            }
        };
        cachedWriter.setEntityManagerFactory(entityManagerFactory);
        return cachedWriter;
    }

}
