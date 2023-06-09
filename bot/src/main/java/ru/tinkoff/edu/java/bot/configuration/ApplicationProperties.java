package ru.tinkoff.edu.java.bot.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationProperties(@NotNull String test,
                                    @NotNull Scrapper scrapper,
                                    @NotNull Bot bot,
                                    @NotNull String queueName,
                                    @NotNull String exchangeName) {

    public record Bot(@NotNull String token) {
    }

    public record Scrapper(@NotNull String url) {
    }

}