package no.fintlabs.validation.instancefield.validators;

import no.fintlabs.model.configuration.entities.ValueMapping;
import no.fintlabs.model.metadata.InstanceElementMetadata;
import no.fintlabs.validation.instancefield.InstanceFieldReferenceTypeCompatibilityValidator;

import java.util.Set;

import static no.fintlabs.model.metadata.InstanceElementMetadata.Type.*;

public class FileInstanceFieldReferenceTypeCompatibilityValidator implements InstanceFieldReferenceTypeCompatibilityValidator {

    @Override
    public ValueMapping.Type getTypeToValidate() {
        return ValueMapping.Type.FILE;
    }

    @Override
    public Set<InstanceElementMetadata.Type> getCompatibleTypes() {
        return Set.of(
                FILE
        );
    }

}
