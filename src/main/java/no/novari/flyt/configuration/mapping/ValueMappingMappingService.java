package no.novari.flyt.configuration.mapping;

import no.novari.flyt.configuration.model.configuration.dtos.ValueMappingDto;
import no.novari.flyt.configuration.model.configuration.entities.ValueMapping;
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
