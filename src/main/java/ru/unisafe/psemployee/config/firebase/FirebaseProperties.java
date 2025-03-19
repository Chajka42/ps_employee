package ru.unisafe.psemployee.config.firebase;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Data
@Configuration
@ConfigurationProperties(prefix = "firebase")
public class FirebaseProperties {
    private Resource serviceAccount;
}