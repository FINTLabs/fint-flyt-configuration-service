package no.fintlabs.model.configuration.dtos.object;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Builder
@EqualsAndHashCode
@Jacksonized
public class ObjectCollectionMappingDto {

    @Builder.Default
    private final Collection<@Valid @NotNull ObjectMappingDto> objectMappings = new ArrayList<>();

    @Builder.Default
    private final Collection<@Valid @NotNull ObjectsFromCollectionMappingDto> objectsFromCollectionMappings = new ArrayList<>();
}
