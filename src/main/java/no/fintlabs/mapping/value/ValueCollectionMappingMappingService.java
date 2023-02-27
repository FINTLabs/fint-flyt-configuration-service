package no.fintlabs.mapping.value;

import no.fintlabs.model.configuration.dtos.value.ValueCollectionMappingDto;
import no.fintlabs.model.configuration.entities.value.ValueCollectionMapping;
import org.springframework.stereotype.Service;

@Service
public class ValueCollectionMappingMappingService {

    private final ValueMappingMappingService valueMappingMappingService;
    private final ValuesFromCollectionMappingMappingService valuesFromCollectionMappingMappingService;

    public ValueCollectionMappingMappingService(
            ValueMappingMappingService valueMappingMappingService,
            ValuesFromCollectionMappingMappingService valuesFromCollectionMappingMappingService
    ) {
        this.valueMappingMappingService = valueMappingMappingService;
        this.valuesFromCollectionMappingMappingService = valuesFromCollectionMappingMappingService;
    }

    public ValueCollectionMapping toEntity(ValueCollectionMappingDto valueCollectionMappingDto) {
        return ValueCollectionMapping
                .builder()
                .valueMappings(
                        valueCollectionMappingDto
                                .getValueMappings()
                                .stream()
                                .map(valueMappingMappingService::toEntity)
                                .toList()
                )
                .valuesFromCollectionMappings(
                        valueCollectionMappingDto
                                .getValuesFromCollectionMappings()
                                .stream()
                                .map(valuesFromCollectionMappingMappingService::toEntity)
                                .toList()
                )
                .build();
    }

    public ValueCollectionMappingDto toDto(ValueCollectionMapping valueCollectionMapping) {
        return ValueCollectionMappingDto
                .builder()
                .valueMappings(
                        valueCollectionMapping
                                .getValueMappings()
                                .stream()
                                .map(valueMappingMappingService::toDto)
                                .toList()
                )
                .valuesFromCollectionMappings(
                        valueCollectionMapping
                                .getValuesFromCollectionMappings()
                                .stream()
                                .map(valuesFromCollectionMappingMappingService::toDto)
                                .toList()
                )
                .build();
    }

}
