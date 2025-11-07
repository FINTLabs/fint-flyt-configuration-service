package no.novari.flyt.configuration.validation.valueparsability;

import no.novari.flyt.configuration.model.configuration.dtos.ValueMappingDto;
import no.novari.flyt.configuration.model.configuration.entities.ValueMapping;

public interface ValueParsabilityValidator {

    default boolean isValid(ValueMappingDto valueMappingDto) {
        return valueMappingDto.getType() != getTypeToValidate() ||
                valueMappingDto.getMappingString() == null ||
                isValid(valueMappingDto.getMappingString());
    }

    ValueMapping.Type getTypeToValidate();

    boolean isValid(String value);

}
