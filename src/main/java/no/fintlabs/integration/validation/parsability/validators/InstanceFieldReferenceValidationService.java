package no.fintlabs.integration.validation.parsability.validators;

import no.fintlabs.integration.model.metadata.InstanceElementMetadata;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Service
@Order(1)
public class InstanceFieldReferenceValidationService {

    public boolean isValid(
            Collection<String> configurationInstanceFieldReferences,
            Set<InstanceElementMetadata.Type> acceptableTypes,
            Map<String, InstanceElementMetadata.Type> metadataInstanceFieldTypePerKey
    ) {
        return metadataInstanceFieldTypePerKey.keySet().containsAll(configurationInstanceFieldReferences) &&
                configurationInstanceFieldReferences
                        .stream()
                        .map(metadataInstanceFieldTypePerKey::get)
                        .allMatch(acceptableTypes::contains);
    }

}
