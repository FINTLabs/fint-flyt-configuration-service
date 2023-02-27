package no.fintlabs.validation.instancereference.validators;

import no.fintlabs.model.configuration.entities.value.ValueMapping;
import no.fintlabs.model.metadata.InstanceValueMetadata;
import no.fintlabs.validation.instancereference.InstanceValueTypeCompatibilityValidator;

import java.util.Set;

import static no.fintlabs.model.metadata.InstanceValueMetadata.Type.STRING;

public class ExampleInstanceValueTypeCompatibilityValidator implements InstanceValueTypeCompatibilityValidator {

    @Override
    public ValueMapping.Type getTypeToValidate() {
        return ValueMapping.Type.DYNAMIC_STRING;
    }

    @Override
    public Set<InstanceValueMetadata.Type> getCompatibleTypes() {
        return Set.of(
                STRING
        );
    }

}
