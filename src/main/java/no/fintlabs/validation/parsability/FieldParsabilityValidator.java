package no.fintlabs.validation.parsability;

import no.fintlabs.model.configuration.dtos.FieldConfigurationDto;
import no.fintlabs.model.configuration.entities.FieldConfiguration;

public interface FieldParsabilityValidator {

    default boolean isValid(FieldConfigurationDto fieldConfigurationDto) {
        return fieldConfigurationDto.getType() != getTypeToValidate() ||
                fieldConfigurationDto.getValue() == null ||
                isValid(fieldConfigurationDto.getValue());
    }

    FieldConfiguration.Type getTypeToValidate();

    boolean isValid(String value);

}
