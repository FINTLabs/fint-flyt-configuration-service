package no.fintlabs.mapping.object;

import no.fintlabs.model.configuration.dtos.object.ObjectCollectionMappingDto;
import no.fintlabs.model.configuration.entities.object.ObjectCollectionMapping;
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
        this.objectMappingMappingService = objectMappingMappingService;
        this.objectsFromCollectionMappingMappingService = objectsFromCollectionMappingMappingService;
    }

    public ObjectCollectionMapping toEntity(ObjectCollectionMappingDto objectCollectionMappingDto) {
        return ObjectCollectionMapping
                .builder()
                .objectMappings(
                        objectCollectionMappingDto
                                .getObjectMappings()
                                .stream()
                                .map(objectMappingMappingService::toEntity)
                                .toList()
                )
                .objectsFromCollectionMappings(
                        objectCollectionMappingDto
                                .getObjectsFromCollectionMappings()
                                .stream()
                                .map(objectsFromCollectionMappingMappingService::toEntity)
                                .toList()
                )
                .build();
    }

    public ObjectCollectionMappingDto toDto(ObjectCollectionMapping objectCollectionMapping) {
        return ObjectCollectionMappingDto
                .builder()
                .objectMappings(
                        objectCollectionMapping
                                .getObjectMappings()
                                .stream()
                                .map(objectMappingMappingService::toDto)
                                .toList()
                )
                .objectsFromCollectionMappings(
                        objectCollectionMapping
                                .getObjectsFromCollectionMappings()
                                .stream()
                                .map(objectsFromCollectionMappingMappingService::toDto)
                                .toList()
                )
                .build();
    }

}
