package com.asj.gestionhorarios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "testAuditorAware")
public class TestConfig {

    @Bean
    public AuditorAware<String> testAuditorAware() {
        return new TestAuditorAware();
    }
}
