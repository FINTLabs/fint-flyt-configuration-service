package no.fintlabs.model.configuration.dtos.object;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import no.fintlabs.model.configuration.dtos.value.ValueCollectionMappingDto;
import no.fintlabs.model.configuration.dtos.value.ValueMappingDto;
import no.fintlabs.validation.constraints.AtLeastOneChild;
import no.fintlabs.validation.constraints.UniqueChildrenKeys;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@AtLeastOneChild
@UniqueChildrenKeys
@Getter
@Builder
@EqualsAndHashCode
@Jacksonized
public class ObjectMappingDto {

    @Builder.Default
    private final Map<String, @Valid @NotNull ValueMappingDto> valueMappingPerKey = new HashMap<>();

    @Builder.Default
    private final Map<String, @Valid @NotNull ValueCollectionMappingDto> valueCollectionMappingPerKey = new HashMap<>();

    @Builder.Default
    private final Map<String, @Valid @NotNull ObjectMappingDto> objectMappingPerKey = new HashMap<>();

    @Builder.Default
    private final Map<String, @Valid @NotNull ObjectCollectionMappingDto> objectCollectionMappingPerKey = new HashMap<>();

}
