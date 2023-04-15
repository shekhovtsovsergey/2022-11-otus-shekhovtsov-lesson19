package ru.otus.lesson19.healthcheck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


@Component
public class DBConnectionHealthCheck implements HealthIndicator {
    private DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Health health() {
        try {
            Connection connection = dataSource.getConnection();
            connection.close();
        } catch (SQLException e) {
            return Health.down().withDetail("DB Connection", "Disconnected").build();
        }
        return Health.up().withDetail("DB Connection", "Connected").build();
    }
}
