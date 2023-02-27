package no.fintlabs.validation.instancereference;

import no.fintlabs.model.configuration.dtos.value.ValueMappingDto;
import no.fintlabs.model.configuration.entities.value.ValueMapping;
import no.fintlabs.model.metadata.InstanceValueMetadata;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.*;

public interface InstanceValueTypeCompatibilityValidator {

    default List<Tuple2<String, InstanceValueMetadata.Type>> findIncompatibleInstanceValuesKeyAndType(
            ValueMappingDto valueMappingDto,
            Collection<String> referencedInstanceValueKeys,
            Map<String, InstanceValueMetadata.Type> instanceValueTypePerKey
    ) {
        if (valueMappingDto.getType() != getTypeToValidate()) {
            return Collections.emptyList();
        }
        return referencedInstanceValueKeys.stream().filter(key -> !getCompatibleTypes().contains(instanceValueTypePerKey.get(key))).map(key -> Tuples.of(key, instanceValueTypePerKey.get(key))).toList();
    }

    ValueMapping.Type getTypeToValidate();

    Set<InstanceValueMetadata.Type> getCompatibleTypes();

}
