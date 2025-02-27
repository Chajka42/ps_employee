package ru.unisafe.psemployee.model;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class DatabaseTest {

    @Bean
    public CommandLineRunner testConnection(ConnectionFactory connectionFactory) {
        return args -> {
            Mono.from(connectionFactory.create())
                    .doOnSuccess(conn -> System.out.println("✅ Подключение к БД установлено!"))
                    .doOnError(error -> System.err.println("❌ Ошибка подключения: " + error.getMessage()))
                    .subscribe();
        };
    }
}
