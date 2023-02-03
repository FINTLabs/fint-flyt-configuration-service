package no.fintlabs.validation.parsability;

import no.fintlabs.model.configuration.dtos.ValueMappingDto;
import no.fintlabs.model.configuration.entities.ValueMapping;

public interface FieldParsabilityValidator {

    default boolean isValid(ValueMappingDto valueMappingDto) {
        return valueMappingDto.getType() != getTypeToValidate() ||
                valueMappingDto.getMappingString() == null ||
                isValid(valueMappingDto.getMappingString());
    }

    ValueMapping.Type getTypeToValidate();

    boolean isValid(String value);

}
