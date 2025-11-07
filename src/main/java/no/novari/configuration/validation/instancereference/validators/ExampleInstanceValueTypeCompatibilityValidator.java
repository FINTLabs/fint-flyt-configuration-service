package no.novari.configuration.validation.instancereference.validators;

import no.novari.configuration.model.configuration.entities.ValueMapping;
import no.novari.configuration.model.metadata.InstanceValueMetadata;
import no.novari.configuration.validation.instancereference.InstanceValueTypeCompatibilityValidator;

import java.util.Set;

import static no.novari.configuration.model.metadata.InstanceValueMetadata.Type.STRING;

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
