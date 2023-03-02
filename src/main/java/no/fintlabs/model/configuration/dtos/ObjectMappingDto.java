package no.fintlabs.model.configuration.dtos;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import no.fintlabs.validation.constraints.AtLeastOneChild;
import no.fintlabs.validation.constraints.UniqueChildrenKeys;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@AtLeastOneChild
@UniqueChildrenKeys
@Getter
@Builder
@EqualsAndHashCode
@Jacksonized
public class ObjectMappingDto {

    public ObjectMappingDto(
            Map<String, ValueMappingDto> valueMappingPerKey,
            Map<String, CollectionMappingDto<ValueMappingDto>> valueCollectionMappingPerKey,
            Map<String, ObjectMappingDto> objectMappingPerKey,
            Map<String, CollectionMappingDto<ObjectMappingDto>> objectCollectionMappingPerKey
    ) {
        this.valueMappingPerKey = Optional.ofNullable(valueMappingPerKey).orElse(new HashMap<>());
        this.valueCollectionMappingPerKey = Optional.ofNullable(valueCollectionMappingPerKey).orElse(new HashMap<>());
        this.objectMappingPerKey = Optional.ofNullable(objectMappingPerKey).orElse(new HashMap<>());
        this.objectCollectionMappingPerKey = Optional.ofNullable(objectCollectionMappingPerKey).orElse(new HashMap<>());
    }

    private final Map<String, @Valid @NotNull ValueMappingDto> valueMappingPerKey;
    private final Map<String, @Valid @NotNull CollectionMappingDto<ValueMappingDto>> valueCollectionMappingPerKey;
    private final Map<String, @Valid @NotNull ObjectMappingDto> objectMappingPerKey;
    private final Map<String, @Valid @NotNull CollectionMappingDto<ObjectMappingDto>> objectCollectionMappingPerKey;

}
