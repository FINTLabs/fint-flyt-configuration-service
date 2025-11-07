package no.novari.configuration.validation.valueparsability;

import no.novari.configuration.model.configuration.dtos.ValueMappingDto;
import no.novari.configuration.model.configuration.entities.ValueMapping;

public interface ValueParsabilityValidator {

    default boolean isValid(ValueMappingDto valueMappingDto) {
        return valueMappingDto.getType() != getTypeToValidate() ||
                valueMappingDto.getMappingString() == null ||
                isValid(valueMappingDto.getMappingString());
    }

    ValueMapping.Type getTypeToValidate();

    boolean isValid(String value);

}
