package no.fintlabs.validation.valueparsability;

import no.fintlabs.model.configuration.dtos.value.ValueMappingDto;
import no.fintlabs.model.configuration.entities.value.ValueMapping;

public interface ValueParsabilityValidator {

    default boolean isValid(ValueMappingDto valueMappingDto) {
        return valueMappingDto.getType() != getTypeToValidate() ||
                valueMappingDto.getMappingString() == null ||
                isValid(valueMappingDto.getMappingString());
    }

    ValueMapping.Type getTypeToValidate();

    boolean isValid(String value);

}
