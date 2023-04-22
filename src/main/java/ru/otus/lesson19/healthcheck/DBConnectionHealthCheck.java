package ru.otus.lesson19.healthcheck;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.lesson19.service.DBHealthCheckService;


@Component
@RequiredArgsConstructor
public class DBConnectionHealthCheck implements HealthIndicator {

    private final DBHealthCheckService dbHealthCheckService;

    @Override
    public Health health() {
        return dbHealthCheckService.getDBHealth();
    }
}
