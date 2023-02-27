package no.fintlabs.mapping.object;

import no.fintlabs.mapping.InstanceCollectionReferencesMappingService;
import no.fintlabs.model.configuration.dtos.object.ObjectsFromCollectionMappingDto;
import no.fintlabs.model.configuration.entities.object.ObjectsFromCollectionMapping;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class ObjectsFromCollectionMappingMappingService {

    private final InstanceCollectionReferencesMappingService instanceCollectionReferencesMappingService;
    private final ObjectMappingMappingService objectMappingMappingService;

    public ObjectsFromCollectionMappingMappingService(
            InstanceCollectionReferencesMappingService instanceCollectionReferencesMappingService,
            @Lazy ObjectMappingMappingService objectMappingMappingService
    ) {
        this.instanceCollectionReferencesMappingService = instanceCollectionReferencesMappingService;
        this.objectMappingMappingService = objectMappingMappingService;
    }

    public ObjectsFromCollectionMapping toEntity(
            ObjectsFromCollectionMappingDto objectsFromCollectionMappingDto
    ) {
        return ObjectsFromCollectionMapping
                .builder()
                .instanceCollectionReferencesOrdered(
                        instanceCollectionReferencesMappingService.toEntity(
                                objectsFromCollectionMappingDto.getInstanceCollectionReferencesOrdered()
                        )
                )
                .objectMapping(objectMappingMappingService.toEntity(objectsFromCollectionMappingDto.getObjectMapping()))
                .build();
    }

    public ObjectsFromCollectionMappingDto toDto(ObjectsFromCollectionMapping objectsFromCollectionMapping) {
        return ObjectsFromCollectionMappingDto
                .builder()
                .instanceCollectionReferencesOrdered(
                        instanceCollectionReferencesMappingService.toDto(
                                objectsFromCollectionMapping.getInstanceCollectionReferencesOrdered()
                        )
                )
                .objectMapping(objectMappingMappingService.toDto(objectsFromCollectionMapping.getObjectMapping()))
                .build();
    }

}
