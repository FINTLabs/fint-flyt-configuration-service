package no.fintlabs.mapping;

import no.fintlabs.model.configuration.dtos.CollectionMappingDto;
import no.fintlabs.model.configuration.dtos.ObjectMappingDto;
import no.fintlabs.model.configuration.entities.collection.ObjectCollectionMapping;
import no.fintlabs.model.configuration.entities.collection.ObjectsFromCollectionMapping;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class ObjectCollectionMappingMappingService {

    private final ObjectMappingMappingService objectMappingMappingService;
    private final CollectionMappingMappingService collectionMappingMappingService;

    public ObjectCollectionMappingMappingService(
            @Lazy ObjectMappingMappingService objectMappingMappingService,
            CollectionMappingMappingService collectionMappingMappingService
    ) {
        this.objectMappingMappingService = objectMappingMappingService;
        this.collectionMappingMappingService = collectionMappingMappingService;
    }

    public ObjectCollectionMapping toEntity(CollectionMappingDto<ObjectMappingDto> dto) {
        return collectionMappingMappingService.toEntity(
                objectMappingMappingService::toEntity,
                ObjectCollectionMapping::new,
                ObjectsFromCollectionMapping::new,
                dto
        );
    }

    public CollectionMappingDto<ObjectMappingDto> toDto(ObjectCollectionMapping entity) {
        return collectionMappingMappingService.toDto(
                objectMappingMappingService::toDto,
                entity
        );
    }

}
