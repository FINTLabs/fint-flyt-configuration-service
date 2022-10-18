package no.fintlabs.validation.instancefield.validators;

import no.fintlabs.model.configuration.entities.FieldConfiguration;
import no.fintlabs.model.metadata.InstanceElementMetadata;
import no.fintlabs.validation.instancefield.InstanceFieldReferenceTypeCompatibilityValidator;

import java.util.Set;

import static no.fintlabs.model.metadata.InstanceElementMetadata.Type.*;

public class ExampleInstanceFieldReferenceTypeCompatibilityValidator implements InstanceFieldReferenceTypeCompatibilityValidator {

    @Override
    public FieldConfiguration.Type getTypeToValidate() {
        return FieldConfiguration.Type.DYNAMIC_STRING;
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
