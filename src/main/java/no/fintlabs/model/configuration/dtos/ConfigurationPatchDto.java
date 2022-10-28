package no.fintlabs.model.configuration.dtos;

import lombok.Data;

import java.util.Collection;
import java.util.Optional;

@Data
public class ConfigurationPatchDto {

    private Long integrationMetadataId;

    private Boolean completed;

    private String comment;

    private Collection<ConfigurationElementDto> elements;

    public Optional<Long> getIntegrationMetadataId() {
        return Optional.ofNullable(integrationMetadataId);
    }

    public Optional<Boolean> isCompleted() {
        return Optional.ofNullable(completed);
    }

    public Optional<String> getComment() {
        return Optional.ofNullable(comment);
    }

    public Optional<Collection<ConfigurationElementDto>> getElements() {
        return Optional.ofNullable(elements);
    }

}
