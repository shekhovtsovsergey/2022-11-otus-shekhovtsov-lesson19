package ru.otus.lesson19.service;

import org.springframework.boot.actuate.health.Health;

public interface DBHealthCheckService {
    public Health getDBHealth();

}
