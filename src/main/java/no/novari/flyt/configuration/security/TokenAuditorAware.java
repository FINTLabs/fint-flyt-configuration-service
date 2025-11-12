package no.novari.flyt.configuration.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("tokenAuditorAware")
public class TokenAuditorAware implements AuditorAware<String> {
    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        String v = CurrentAuditor.get();
        return Optional.of((v == null || v.isBlank()) ? "system" : v);
    }
}