package no.fintlabs.model.configuration.dtos;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.Optional;

@Builder
@Jacksonized
public class ConfigurationPatchDto {

    private final Long integrationMetadataId;

    private final Boolean completed;

    private final String comment;

    private final ObjectMappingDto mapping;

    public Optional<Long> getIntegrationMetadataId() {
        return Optional.ofNullable(integrationMetadataId);
    }

    public Optional<Boolean> isCompleted() {
        return Optional.ofNullable(completed);
    }

    public Optional<String> getComment() {
        return Optional.ofNullable(comment);
    }

    public Optional<ObjectMappingDto> getMapping() {
        return Optional.ofNullable(mapping);
    }

}
