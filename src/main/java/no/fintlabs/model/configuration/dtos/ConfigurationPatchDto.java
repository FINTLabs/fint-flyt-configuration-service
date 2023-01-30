package no.fintlabs.model.configuration.dtos;

import lombok.Data;

import java.util.Optional;

@Data
public class ConfigurationPatchDto {

    private Long integrationMetadataId;

    private Boolean completed;

    private String comment;

    private ElementMappingDto mapping;

    public Optional<Long> getIntegrationMetadataId() {
        return Optional.ofNullable(integrationMetadataId);
    }

    public Optional<Boolean> isCompleted() {
        return Optional.ofNullable(completed);
    }

    public Optional<String> getComment() {
        return Optional.ofNullable(comment);
    }

    public Optional<ElementMappingDto> getMapping() {
        return Optional.ofNullable(mapping);
    }

}
