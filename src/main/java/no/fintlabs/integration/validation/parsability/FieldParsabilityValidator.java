package no.fintlabs.integration.validation.parsability;

import no.fintlabs.integration.model.FieldConfiguration;

public interface FieldParsabilityValidator {

    default boolean validate(FieldConfiguration fieldConfiguration) {
        return fieldConfiguration.getType() != getFieldValueType() || validate(fieldConfiguration.getValue());
    }

    FieldConfiguration.Type getFieldValueType();

    boolean validate(String value);

}
