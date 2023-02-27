package no.fintlabs.model.configuration.dtos;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.extern.jackson.Jacksonized;
import no.fintlabs.model.configuration.dtos.object.ObjectMappingDto;

import java.util.Optional;

@Builder
@EqualsAndHashCode
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
