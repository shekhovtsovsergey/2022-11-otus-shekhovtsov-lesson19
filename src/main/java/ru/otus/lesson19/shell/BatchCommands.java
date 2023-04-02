package ru.otus.lesson19.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import static ru.otus.lesson19.config.CsvToSqlJobConfig.INPUT_FILE_NAME;

@ShellComponent
@RequiredArgsConstructor
public class BatchCommands {

    private final Job loadDataToMongo;
    private final Job loadDataToSql;
    private final Job loadDataCsvToSql;
    private final JobLauncher jobLauncher;

    @ShellMethod(value = "Migrate data from relational database to MongoDB", key = "sj")
    public void startLoadDataToMongoJob() throws Exception {
        JobExecution execution = jobLauncher.run(loadDataToMongo, new JobParameters());
        System.out.println(execution);
    }

    @ShellMethod(value = "Migrate data from MongoDB database to relational", key = "sj2")
    public void startLoadDataToSqlJob() throws Exception {
        JobExecution execution = jobLauncher.run(loadDataToSql, new JobParameters());
        System.out.println(execution);
    }

    @ShellMethod(value = "Migrate data from Csv to relational", key = "sj3")
    public void startLoadDataCsvToSqlJob(@ShellOption String inputFileName) throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString(INPUT_FILE_NAME, inputFileName)
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        JobExecution execution = jobLauncher.run(loadDataCsvToSql, jobParameters);
        System.out.println(execution);
    }
}
