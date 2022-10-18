package no.fintlabs.validation.instancefield;

import no.fintlabs.model.configuration.dtos.FieldConfigurationDto;
import no.fintlabs.model.configuration.entities.FieldConfiguration;
import no.fintlabs.model.metadata.InstanceElementMetadata;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.*;

public interface InstanceFieldReferenceTypeCompatibilityValidator {

    default List<Tuple2<String, InstanceElementMetadata.Type>> findIncompatibleInstanceFieldsKeyAndType(
            FieldConfigurationDto fieldConfigurationDto,
            Collection<String> configurationInstanceFieldReferenceKeys,
            Map<String, InstanceElementMetadata.Type> metadataInstanceFieldTypePerKey
    ) {
        if (fieldConfigurationDto.getType() != getTypeToValidate()) {
            return Collections.emptyList();
        }

        return configurationInstanceFieldReferenceKeys
                .stream()
                .filter(key -> !getCompatibleTypes().contains(metadataInstanceFieldTypePerKey.get(key)))
                .map(key -> Tuples.of(key, metadataInstanceFieldTypePerKey.get(key)))
                .toList();
    }

    FieldConfiguration.Type getTypeToValidate();

    Set<InstanceElementMetadata.Type> getCompatibleTypes();

}
