package no.fintlabs.integration.validation.instancefield;

import no.fintlabs.integration.model.configuration.FieldConfiguration;
import no.fintlabs.integration.model.metadata.InstanceElementMetadata;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.*;

public interface InstanceFieldReferenceTypeCompatibilityValidator {

    default List<Tuple2<String, InstanceElementMetadata.Type>> findIncompatibleInstanceFieldsKeyAndType(
            FieldConfiguration fieldConfiguration,
            Collection<String> configurationInstanceFieldReferenceKeys,
            Map<String, InstanceElementMetadata.Type> metadataInstanceFieldTypePerKey
    ) {
        if (fieldConfiguration.getType() != getTypeToValidate()) {
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
