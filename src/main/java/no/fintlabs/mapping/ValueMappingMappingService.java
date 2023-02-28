package no.fintlabs.mapping;

import no.fintlabs.model.configuration.dtos.ValueMappingDto;
import no.fintlabs.model.configuration.entities.ValueMapping;
import org.springframework.stereotype.Service;

@Service
public class ValueMappingMappingService implements ElementMappingMappingService<ValueMapping, ValueMappingDto> {

    public ValueMapping toEntity(ValueMappingDto valueMappingDto) {
        return ValueMapping
                .builder()
                .type(valueMappingDto.getType())
                .mappingString(valueMappingDto.getMappingString())
                .build();
    }

    public ValueMappingDto toDto(ValueMapping valueMapping) {
        return ValueMappingDto
                .builder()
                .type(valueMapping.getType())
                .mappingString(valueMapping.getMappingString())
                .build();
    }

}
