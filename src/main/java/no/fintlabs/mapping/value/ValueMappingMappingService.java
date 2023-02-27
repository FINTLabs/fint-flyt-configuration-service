package no.fintlabs.mapping.value;

import no.fintlabs.model.configuration.dtos.value.ValueMappingDto;
import no.fintlabs.model.configuration.entities.value.ValueMapping;
import org.springframework.stereotype.Service;

@Service
public class ValueMappingMappingService {

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
