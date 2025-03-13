package ru.unisafe.psemployee;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition
@SpringBootApplication
public class PsEmployeeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PsEmployeeApplication.class, args);
    }

}
