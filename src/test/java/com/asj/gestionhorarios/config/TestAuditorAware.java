package com.asj.gestionhorarios.config;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class TestAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("test-user");
    }
}
