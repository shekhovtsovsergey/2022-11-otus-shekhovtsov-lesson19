package ru.otus.lesson19.config;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
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
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.ListUtils;
import ru.otus.lesson19.service.CacheService;


@Configuration
@RequiredArgsConstructor
public class MongoToSqlJobConfig {

    private static final int CHUNK_SIZE = 3000;
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
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        executor.initialize();

        return stepBuilderFactory.get("loadBooksToSql")
                .<BookMongo, Book>chunk(CHUNK_SIZE)
                .reader(bookMongoReader())
                .processor(bookMongoProcessor())
                .writer(bookSqlWriter(cacheService))
                .taskExecutor(new SimpleAsyncTaskExecutor()) // задаем TaskExecutor
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
            return new Book(
                    null,
                    bookMongo.getName(),
                    new Author(null, bookMongo.getAuthor().getName()),
                    new Genre(null, bookMongo.getGenre().getName()),
                    new ArrayList<>()
            );
        };
    }

    @Bean
    @Transactional
    public ItemWriter<Book> bookSqlWriter(CacheService cacheService) {
        return new ItemWriter<Book>() {
            private final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            {
                executor.setCorePoolSize(20);
                executor.setMaxPoolSize(40);
                executor.setQueueCapacity(100);
                executor.initialize();

            }

            @Override
            public void write(List<? extends Book> books) throws Exception {
                int threadCount = Math.min(executor.getMaxPoolSize(), books.size());
                List<? extends List<? extends Book>> bookSubLists = ListUtils.partition(books, threadCount);

                List<Future<?>> futures = new ArrayList<>();
                for (List<? extends Book> bookSubList : bookSubLists) {
                    Future<?> future = executor.submit(() -> {
                        for (Book book : bookSubList) {

                            Author author = cacheService.getAuthorByName(book.getAuthor().getName());
                            if (author == null) {

                                author = authorDao.findFirstByName(book.getAuthor().getName());
                                if (author == null) {
                                    throw new EntityNotFoundException("Author not found");
                                }

                                cacheService.putAuthor(author);
                            }
                            book.setAuthor(author);


                            Genre genre = cacheService.getGenreByName(book.getGenre().getName());
                            if (genre == null) {

                                genre = genreDao.findFirstByName(book.getGenre().getName());
                                if (genre == null) {
                                    throw new EntityNotFoundException("Genre not found");
                                }

                                cacheService.putGenre(genre);
                            }
                            book.setGenre(genre);
                        }
                        for (Book book : bookSubList) {
                            bookDao.save(book);
                        }
                    });
                    futures.add(future);
                }

                for (Future<?> future : futures) {
                    future.get();
                }
            }
        };
    }
}
