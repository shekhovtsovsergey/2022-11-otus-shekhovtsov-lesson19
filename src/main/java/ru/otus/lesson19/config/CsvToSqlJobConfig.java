package ru.otus.lesson19.config;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.mongodb.core.MongoOperations;
import ru.otus.lesson19.dao.sql.AuthorDao;
import ru.otus.lesson19.dao.sql.BookDao;
import ru.otus.lesson19.dao.sql.GenreDao;
import ru.otus.lesson19.dto.BookDto;
import ru.otus.lesson19.model.sql.Author;
import ru.otus.lesson19.model.sql.Book;
import ru.otus.lesson19.model.sql.Comment;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;

@Configuration
@RequiredArgsConstructor
public class CsvToSqlJobConfig {

    private static final int CHUNK_SIZE = 100;
    public static final String JOB_NAME = "loadDataCsvToSql";
    public static final String INPUT_FILE_NAME = "inputFileName";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final EntityManagerFactory entityManagerFactory;
    private final MongoOperations mongoOperations;


    @Bean
    public Job loadDataCsvToSql() {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(loadBooksCsvToSql())
                .build();
    }
    @Bean
    public Step loadBooksCsvToSql() {
        return stepBuilderFactory.get("loadDataCsvToSql")
                .<BookDto, Book>chunk(CHUNK_SIZE)
                .reader(bookCsvReader(INPUT_FILE_NAME))
                .processor(bookCsvProcessor())
                .writer(bookCsvToSqlWriter())
                .build();
    }

    @StepScope
    @Bean
    public FlatFileItemReader<BookDto> bookCsvReader(@Value("#{jobParameters['" + INPUT_FILE_NAME + "']}") String inputFileName) {
        var reader = new FlatFileItemReader<BookDto>();
        reader.setResource(new FileSystemResource(inputFileName));
        reader.setLinesToSkip(1);
        reader.setLineMapper(new DefaultLineMapper<BookDto>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setDelimiter(",");
                setNames(new String[]{"", "name", "author", "genre"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<BookDto>() {{
                setTargetType(BookDto.class);
            }});
        }});
        return reader;
    }

    @Bean
    public ItemProcessor<BookDto, Book> bookCsvProcessor() {
        return bookDto -> {
            Author author = authorDao.findFirstByName(bookDto.getAuthor());
            if (author == null) {
                throw new EntityNotFoundException("Author not found");
            }
            var genre = genreDao.findFirstByName(bookDto.getGenre());
            if (genre == null) {
                throw new EntityNotFoundException("Genre not found");
            }
            return new Book(null, bookDto.getName(), author, genre, new ArrayList<Comment>());
        };
    }

    @Bean
    public JpaItemWriter<Book> bookCsvToSqlWriter() {
        var writer = new JpaItemWriter<Book>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
}
