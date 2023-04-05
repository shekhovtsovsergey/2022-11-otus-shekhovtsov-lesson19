package ru.otus.lesson19.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@RequiredArgsConstructor
public class BatchCommands {

    private final Job loadDataToSql;
    private final JobLauncher jobLauncher;

    @ShellMethod(value = "Migrate data from MongoDB database to sql", key = "sj")
    public void startLoadDataToSqlJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        JobExecution execution = jobLauncher.run(loadDataToSql, jobParameters);
        System.out.println(execution);
    }

}
