package no.fintlabs.model.configuration.dtos.value;

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
public class ValueCollectionMappingDto {

    @Builder.Default
    private final Collection<@Valid @NotNull ValueMappingDto> valueMappings = new ArrayList<>();

    @Builder.Default
    private final Collection<@Valid @NotNull ValuesFromCollectionMappingDto> valuesFromCollectionMappings = new ArrayList<>();

}
