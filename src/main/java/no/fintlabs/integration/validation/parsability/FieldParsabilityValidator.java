package no.fintlabs.integration.validation.parsability;

import no.fintlabs.integration.model.configuration.dtos.FieldConfigurationDto;
import no.fintlabs.integration.model.configuration.entities.FieldConfiguration;

public interface FieldParsabilityValidator {

    default boolean isValid(FieldConfigurationDto fieldConfigurationDto) {
        return fieldConfigurationDto.getType() != getTypeToValidate() || isValid(fieldConfigurationDto.getValue());
    }

    FieldConfiguration.Type getTypeToValidate();

    boolean isValid(String value);

}
