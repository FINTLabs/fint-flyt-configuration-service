package no.novari.flyt.configuration.mapping;

import no.novari.flyt.configuration.model.configuration.dtos.CollectionMappingDto;
import no.novari.flyt.configuration.model.configuration.dtos.ObjectMappingDto;
import no.novari.flyt.configuration.model.configuration.entities.collection.ObjectCollectionMapping;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class ObjectCollectionMappingMappingService {

    private final ObjectMappingMappingService objectMappingMappingService;
    private final ObjectsFromCollectionMappingMappingService objectsFromCollectionMappingMappingService;

    public ObjectCollectionMappingMappingService(
            @Lazy ObjectMappingMappingService objectMappingMappingService,
            ObjectsFromCollectionMappingMappingService objectsFromCollectionMappingMappingService
    ) {
        this.objectsFromCollectionMappingMappingService = objectsFromCollectionMappingMappingService;
        this.objectMappingMappingService = objectMappingMappingService;
    }


    public ObjectCollectionMapping toEntity(CollectionMappingDto<ObjectMappingDto> objectCollectionMappingDto) {
        return ObjectCollectionMapping
                .builder()
                .objectMappings(
                        objectCollectionMappingDto
                                .getElementMappings()
                                .stream()
                                .map(objectMappingMappingService::toEntity)
                                .toList()
                )
                .objectsFromCollectionMappings(
                        objectCollectionMappingDto
                                .getFromCollectionMappings()
                                .stream()
                                .map(objectsFromCollectionMappingMappingService::toEntity)
                                .toList()
                )
                .build();
    }

    public CollectionMappingDto<ObjectMappingDto> toDto(ObjectCollectionMapping objectCollectionMapping) {
        return CollectionMappingDto
                .<ObjectMappingDto>builder()
                .elementMappings(
                        objectCollectionMapping
                                .getObjectMappings()
                                .stream()
                                .map(objectMappingMappingService::toDto)
                                .toList()
                )
                .fromCollectionMappings(
                        objectCollectionMapping
                                .getObjectsFromCollectionMappings()
                                .stream()
                                .map(objectsFromCollectionMappingMappingService::toDto)
                                .toList()
                )
                .build();
    }

}
