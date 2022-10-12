package no.fintlabs.integration.validation.parsability;

import no.fintlabs.integration.model.configuration.FieldConfiguration;

public interface FieldParsabilityValidator {

    default boolean isValid(FieldConfiguration fieldConfiguration) {
        return fieldConfiguration.getType() != getTypeToValidate() || isValid(fieldConfiguration.getValue());
    }

    FieldConfiguration.Type getTypeToValidate();

    boolean isValid(String value);

}
