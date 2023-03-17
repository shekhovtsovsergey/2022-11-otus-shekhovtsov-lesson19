package ru.otus.lesson27.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(MailProperties.PREFIX)
@Getter
@Setter
public class MailProperties {
    public static final String PREFIX = "mail";
    private String username;
    private String password;
}
