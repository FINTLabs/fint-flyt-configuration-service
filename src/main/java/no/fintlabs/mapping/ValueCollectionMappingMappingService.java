package no.fintlabs.mapping;

import no.fintlabs.model.configuration.dtos.CollectionMappingDto;
import no.fintlabs.model.configuration.dtos.ValueMappingDto;
import no.fintlabs.model.configuration.entities.collection.ValueCollectionMapping;
import no.fintlabs.model.configuration.entities.collection.ValuesFromCollectionMapping;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class ValueCollectionMappingMappingService {

    private final ValueMappingMappingService valueMappingMappingService;
    private final CollectionMappingMappingService collectionMappingMappingService;

    public ValueCollectionMappingMappingService(
            @Lazy ValueMappingMappingService valueMappingMappingService,
            CollectionMappingMappingService collectionMappingMappingService
    ) {
        this.valueMappingMappingService = valueMappingMappingService;
        this.collectionMappingMappingService = collectionMappingMappingService;
    }

    public ValueCollectionMapping toEntity(CollectionMappingDto<ValueMappingDto> dto) {
        return collectionMappingMappingService.toEntity(
                valueMappingMappingService,
                dto,
                ValueCollectionMapping::new,
                ValuesFromCollectionMapping::new
        );
    }

    public CollectionMappingDto<ValueMappingDto> toDto(ValueCollectionMapping entity) {
        return collectionMappingMappingService.toDto(
                valueMappingMappingService,
                entity
        );
    }

}
