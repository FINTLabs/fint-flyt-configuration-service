package no.fintlabs.integration.validation.instancefield.validators;

import no.fintlabs.integration.model.configuration.FieldConfiguration;
import no.fintlabs.integration.model.metadata.InstanceElementMetadata;
import no.fintlabs.integration.validation.instancefield.InstanceFieldReferenceTypeCompatibilityValidator;
import org.springframework.stereotype.Service;

import java.util.Set;

import static no.fintlabs.integration.model.metadata.InstanceElementMetadata.Type.*;

@Service
public class DynamicStringInstanceFieldReferenceTypeCompatibilityValidator implements InstanceFieldReferenceTypeCompatibilityValidator {

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
