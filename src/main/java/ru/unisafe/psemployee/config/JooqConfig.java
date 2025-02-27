package ru.unisafe.psemployee.config;

import io.r2dbc.spi.ConnectionFactory;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig {

    @Bean
    public DSLContext dslContext(ConnectionFactory connectionFactory) {
        return new DefaultDSLContext(
                new DefaultConfiguration()
                        .set(SQLDialect.MYSQL)
                        .set(connectionFactory)
        );
    }
}
