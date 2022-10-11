package no.fintlabs.integration.validation.parsability;

import no.fintlabs.integration.model.FieldConfiguration;

public interface FieldParsabilityValidator {

    default boolean isValid(FieldConfiguration fieldConfiguration) {
        return fieldConfiguration.getType() != getFieldValueType() || isValid(fieldConfiguration.getValue());
    }

    FieldConfiguration.Type getFieldValueType();

    boolean isValid(String value);

}
