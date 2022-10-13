package no.fintlabs.integration.validation.parsability;

import no.fintlabs.integration.model.configuration.FieldConfiguration;
import no.fintlabs.integration.validation.ConfigurationValidationContext;

public interface FieldParsabilityValidator {

    default boolean isValid(FieldConfiguration fieldConfiguration, ConfigurationValidationContext configurationValidationContext) {
        return fieldConfiguration.getType() != getTypeToValidate() || isValid(fieldConfiguration.getValue(), configurationValidationContext);
    }

    FieldConfiguration.Type getTypeToValidate();

    boolean isValid(String value, ConfigurationValidationContext configurationValidationContext);

}
