package no.novari.configuration.mapping;

import no.novari.configuration.model.configuration.dtos.CollectionMappingDto;
import no.novari.configuration.model.configuration.dtos.ValueMappingDto;
import no.novari.configuration.model.configuration.entities.collection.ValueCollectionMapping;
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

    public ValueCollectionMapping toEntity(CollectionMappingDto<ValueMappingDto> valueCollectionMappingDto) {
        return ValueCollectionMapping
                .builder()
                .valueMappings(
                        valueCollectionMappingDto
                                .getElementMappings()
                                .stream()
                                .map(valueMappingMappingService::toEntity)
                                .toList()
                )
                .valuesFromCollectionMappings(
                        valueCollectionMappingDto
                                .getFromCollectionMappings()
                                .stream()
                                .map(valuesFromCollectionMappingMappingService::toEntity)
                                .toList()
                )
                .build();
    }

    public CollectionMappingDto<ValueMappingDto> toDto(ValueCollectionMapping valueCollectionMapping) {
        return CollectionMappingDto
                .<ValueMappingDto>builder()
                .elementMappings(
                        valueCollectionMapping
                                .getValueMappings()
                                .stream()
                                .map(valueMappingMappingService::toDto)
                                .toList()
                )
                .fromCollectionMappings(
                        valueCollectionMapping
                                .getValuesFromCollectionMappings()
                                .stream()
                                .map(valuesFromCollectionMappingMappingService::toDto)
                                .toList()
                )
                .build();
    }

}
