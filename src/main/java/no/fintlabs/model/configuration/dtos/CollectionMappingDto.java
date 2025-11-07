package no.fintlabs.model.configuration.dtos;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Getter
@Builder
@EqualsAndHashCode
@Jacksonized
public class CollectionMappingDto<T> {

    public CollectionMappingDto(
            Collection<T> elementMappings,
            Collection<FromCollectionMappingDto<T>> fromCollectionMappings
    ) {
        this.elementMappings = Optional.ofNullable(elementMappings).orElse(new ArrayList<>());
        this.fromCollectionMappings = Optional.ofNullable(fromCollectionMappings).orElse(new ArrayList<>());
    }

    private final Collection<@Valid @NotNull T> elementMappings;

    private final Collection<@Valid @NotNull FromCollectionMappingDto<T>> fromCollectionMappings;

}
