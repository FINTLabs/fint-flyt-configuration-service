package no.novari.configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Optional;

@Builder
@AllArgsConstructor
public class ConfigurationFilter {

    private final Long integrationId;
    private final Boolean completed;

    public Optional<Long> getIntegrationId() {
        return Optional.ofNullable(integrationId);
    }

    public Optional<Boolean> getCompleted() {
        return Optional.ofNullable(completed);
    }

}
