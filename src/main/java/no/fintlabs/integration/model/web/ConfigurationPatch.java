package no.fintlabs.integration.model.web;

import lombok.Data;
import no.fintlabs.integration.model.ConfigurationElement;

import java.util.Collection;
import java.util.Optional;

@Data
public class ConfigurationPatch {

    private Long integrationMetadataId;

    private Boolean completed;

    private String comment;

    private Collection<ConfigurationElement> elements;

    public Optional<Long> getIntegrationMetadataId() {
        return Optional.ofNullable(integrationMetadataId);
    }

    public Optional<Boolean> isCompleted() {
        return Optional.ofNullable(completed);
    }

    public Optional<String> getComment() {
        return Optional.ofNullable(comment);
    }

    public Optional<Collection<ConfigurationElement>> getElements() {
        return Optional.ofNullable(elements);
    }

}
