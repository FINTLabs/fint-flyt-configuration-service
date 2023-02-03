package no.fintlabs.validation.instancefield.validators;

import no.fintlabs.model.configuration.entities.ValueMapping;
import no.fintlabs.model.metadata.InstanceElementMetadata;
import no.fintlabs.validation.instancefield.InstanceFieldReferenceTypeCompatibilityValidator;

import java.util.Set;

import static no.fintlabs.model.metadata.InstanceElementMetadata.Type.*;

public class ExampleInstanceFieldReferenceTypeCompatibilityValidator implements InstanceFieldReferenceTypeCompatibilityValidator {

    @Override
    public ValueMapping.Type getTypeToValidate() {
        return ValueMapping.Type.DYNAMIC_STRING;
    }

    @Override
    public Set<InstanceElementMetadata.Type> getCompatibleTypes() {
        return Set.of(
                STRING,
                DATE,
                DATETIME,
                URL,
                EMAIL,
                PHONE,
                INTEGER,
                DOUBLE
        );
    }

}
