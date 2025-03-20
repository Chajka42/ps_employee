package ru.unisafe.psemployee;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class WebFluxConfig implements WebFluxConfigurer {

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }
}
