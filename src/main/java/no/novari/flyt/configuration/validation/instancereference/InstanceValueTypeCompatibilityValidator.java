package no.novari.flyt.configuration.validation.instancereference;

import no.novari.flyt.configuration.model.configuration.dtos.ValueMappingDto;
import no.novari.flyt.configuration.model.configuration.entities.ValueMapping;
import no.novari.flyt.configuration.model.metadata.InstanceValueMetadata;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
