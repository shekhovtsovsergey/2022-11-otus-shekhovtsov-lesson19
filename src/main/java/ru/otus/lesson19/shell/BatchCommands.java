package ru.otus.lesson19.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@RequiredArgsConstructor
public class BatchCommands {

    private final Job loadDataToMongo;
    private final Job loadDataToSql;
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
}
